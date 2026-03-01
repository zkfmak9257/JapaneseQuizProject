# JapaneseQuizProject DevOps/CI-CD 진행 기록

## 1. 전체 개념 요약
- 본 기록은 학습 목적의 `DevOps(개발·운영 통합)` 및 `CI/CD(Continuous Integration/Continuous Delivery, 지속적 통합/지속적 제공)` 적용 과정을 정리합니다.
- 현재 범위는 `Docker(도커)` 기반 백엔드/프론트 컨테이너화와 `Docker Compose(도커 컴포즈)` 통합 실행까지입니다.

## 2. 왜 필요한지 (문제 -> 해결)
- 문제:
  - 로컬 수동 실행만으로는 환경 재현성과 배포 일관성이 낮음
  - 서비스별 실행 명령이 분산되어 운영/검증이 번거로움
- 해결:
  - 백엔드/프론트를 이미지로 표준화
  - 컴포즈로 DB/백엔드/프론트를 선언형으로 통합 실행

## 3. 적용 범위 및 완료 항목

### 3.1 백엔드 빌드 산출물 전략 정리
- `bootJar`를 활성화하고 `jar`를 비활성화하여 실행 가능한 단일 JAR 산출물 생성
- 확인 결과:
  - `build/libs/jpquiz-0.0.1-SNAPSHOT.jar` 생성 확인

### 3.2 백엔드 Dockerfile 구성
- 멀티 스테이지 빌드 적용
  - 빌드 스테이지: `gradle:8-jdk17`
  - 런타임 스테이지: `eclipse-temurin:17-jre`
- 목적:
  - 이미지 경량화
  - 빌드 도구와 런타임 분리

### 3.3 백엔드 컨테이너 실행 검증
- 실행 시 DB 연결 실패 이슈 확인 및 해결
  - 원인: 컨테이너 내부에서 `localhost`는 호스트 DB가 아님
  - 조치: 환경변수 주입(`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET`)
- 포트 충돌(8080) 이슈 확인 후 재시도하여 정상 기동

### 3.4 프론트 Dockerfile 구성
- 멀티 스테이지 빌드 적용
  - 빌드 스테이지: `node:lts-alpine`
  - 런타임 스테이지: `nginx:stable-alpine`
- `nginx.conf`를 통한 SPA 라우팅 처리(`try_files ... /index.html`)
- 컨테이너 실행 검증 완료

### 3.5 Docker Compose 통합 구성
- 서비스 구성:
  - `mariadb`
  - `backend`
  - `frontend`
- 반영 항목:
  - 서비스 간 네트워크 연결
  - DB 헬스체크(Health Check, 상태 점검)
  - `depends_on` 기반 기동 순서 제어
  - `named volume(네임드 볼륨)`으로 DB 데이터 영속성 확보
- 이슈 해결:
  - `mariadb_data undefined` -> 루트 `volumes:` 선언 추가
  - `3306 port already in use` -> 호스트 포트 충돌 인지 및 조정
- 최종 상태:
  - `docker compose up -d` 기준 통합 기동 성공

## 4. 트러블슈팅 요약
- `no match for platform in manifest`:
  - `eclipse-temurin:17-jre-alpine` 대신 `eclipse-temurin:17-jre` 사용
- `address already in use(8080/3306)`:
  - 점유 프로세스/컨테이너 확인 후 포트 조정
- `JDBC Connection refused`:
  - 컨테이너 런타임 환경변수로 DB 연결정보 명시
- `undefined volume mariadb_data`:
  - 루트 레벨 `volumes:` 블록 누락 수정

## 5. 현재 산출물
- `Dockerfile` (백엔드)
- `frontend/Dockerfile`
- `frontend/nginx.conf`
- `docker-compose.yml`
- `.env` (로컬 실행용, 민감정보는 커밋 제외)
- 학습 가이드:
  - `DEVOPS_CICD_LEARNING_GUIDE.md`

## 6. 다음 단계 (CI 관점)
- `Jenkinsfile` 작성 및 파이프라인 1차 구성
  - Checkout
  - Backend Test
  - Frontend Build
- 이후 2차 확장
  - Docker Build/Push
  - Manifest 업데이트
  - ArgoCD 연동

## 7. 학습 관점 코멘트
- 성능/확장성/유지보수 측면에서, 현재 단계는 “실행 표준화”를 완료한 상태입니다.
- 이후 CI/CD를 붙이면 변경 검증과 배포 재현성이 크게 향상됩니다.

## 8. 포트폴리오 핵심 성과 (2026-03-01)
### 8.1 GitOps 배포 경로 확립
- `manifests repository(매니페스트 저장소)`를 분리 운영하고 `Argo CD(아르고CD)`로 자동 동기화 배포를 구성했습니다.
- 최종 상태: `jpquiz-app` 기준 `Sync Status: Synced`, `Health Status: Healthy` 확인 완료.

### 8.2 Kubernetes 라우팅/포트 정합성 복구
- 증상: `503 Service Temporarily Unavailable` 발생.
- 원인: `Ingress(인그레스)` 경로와 `frontend Service(프론트엔드 서비스)` 포트 불일치.
- 조치: `jpquiz-frontend`의 `containerPort/service port/targetPort`를 `80`으로 통일하고 Ingress 경로를 점검했습니다.
- 결과: `Ingress -> Frontend -> Backend` 요청 경로 정상화.

### 8.3 프론트 API 진입점 표준화
- 증상: `ERR_CONNECTION_TIMED_OUT` (`192.168.0.11:8080` 고정 호출).
- 원인: 프론트 환경변수에 로컬 IP/포트 하드코딩.
- 조치: `VITE_API_BASE_URL` 기본값을 `/boot`로 전환해 Ingress 단일 진입점(80) 기준으로 정리했습니다.
- 결과: 환경 의존 IP 변경 이슈 제거 및 배포 환경 일관성 확보.

### 8.4 DB 스키마/데이터 복구 및 원인 분리
- 증상: 퀴즈 시작 API 500.
- 원인1: `quiz_questions` 테이블 누락.
- 조치1: `schema.sql`/`seed_word_sentence.sql` 재적재 후 `question_count=160` 확인.
- 원인2: `FIND_IN_SET` 실행 시 `collation(문자 정렬 규칙)` 충돌.
- 조치2: 매퍼 SQL에서 비교 인자의 collation을 명시적으로 통일하도록 코드 수정.

### 8.5 운영 검증 기준 단일화
- Kubernetes DB와 Docker Compose DB를 혼용하면 데이터 검증 혼선이 발생함을 확인했습니다.
- 포트폴리오 검증 기준을 `Kubernetes DB(jpquiz namespace)`로 단일화하여 장애 원인 추적 시간을 단축했습니다.

### 8.6 Argo CD 설치 이슈 복구
- 증상: `argocd-applicationset-controller`가 `CrashLoopBackOff`로 반복 실패.
- 원인: `applicationsets.argoproj.io CRD(CustomResourceDefinition, 사용자 정의 리소스 정의)` 누락.
- 조치: CRD를 별도 적용하고 컨트롤러 롤아웃 재시작.
- 결과: `argocd` 주요 컴포넌트 `Running` 복구, `ApplicationSet` 리소스 인식 정상화.

### 8.7 Argo CD 접속/세션 이슈 정리
- 증상1: `localhost:8888` 프록시 연결 거부(`connect: connection refused`).
- 원인1: 프록시 설정값과 실제 포트포워딩 세션 불일치.
- 조치1: `kubectl port-forward svc/argocd-server -n argocd 8888:443` 기준으로 접속 경로 통일.
- 증상2: `argocd app sync` 시 `token is expired`.
- 조치2: `argocd login localhost:8888 --username admin --insecure` 재인증 후 동기화.

### 8.8 Namespace 정합성 이슈 복구
- 증상: `ingress jpquiz/jpquiz-ingress` 중복 충돌 및 `NotFound` 혼재.
- 원인: 일부 리소스가 `default namespace(기본 네임스페이스)`에 잘못 생성됨.
- 조치: `default` 리소스 삭제 후 `-n jpquiz` 기준으로 재적용.
- 결과: `deploy/service/ingress` 리소스를 `jpquiz`로 일원화.

### 8.9 Frontend Nginx 설정 오타 수정
- 증상: 프론트 접근 시 `500 Internal Server Error (nginx/1.28.2)`.
- 원인: `nginx.conf`의 정적 루트 경로 오타(`root /user/share/nginx/html;`).
- 조치: `root /usr/share/nginx/html;`로 수정 후 이미지 재빌드/재배포.
- 결과: 정적 자산 서빙 정상화.

### 8.10 회원가입 데이터 미반영 오해 해소
- 증상: 회원가입 성공 후 DB에서 사용자 미조회로 인식.
- 원인: 실제 저장 대상은 Kubernetes DB인데 Docker Compose DB를 조회해 혼선 발생.
- 검증: `kubectl exec -n jpquiz deploy/mariadb ... SELECT ...`로 계정 존재 확인, 백엔드 로그 `회원가입 완료` 확인.
- 결론: 기능 실패가 아니라 조회 대상 DB 불일치 이슈.
