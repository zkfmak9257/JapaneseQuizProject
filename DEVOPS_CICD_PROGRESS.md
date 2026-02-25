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
