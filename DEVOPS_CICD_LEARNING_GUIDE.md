# JapaneseQuizProject DevOps 학습 가이드

이 문서는 **학습 목적**으로 `JapaneseQuizProject`에 `DevOps(개발·운영 통합)`와 `CI/CD(Continuous Integration/Continuous Delivery, 지속적 통합/지속적 제공)`를 **처음부터 단계적으로** 적용하기 위한 안내서입니다.

---

## 1. 전체 개념 요약

이 프로젝트에서 목표로 하는 흐름은 아래와 같습니다.

1. 개발자가 코드를 `GitHub`에 Push합니다.
2. `Jenkins(젠킨스, CI 서버)`가 자동으로 빌드/테스트를 수행합니다.
3. 테스트가 통과하면 `Docker Image(도커 이미지)`를 빌드하고 `DockerHub`에 Push합니다.
4. 배포용 `Manifest(매니페스트, 쿠버네티스 선언 파일)` 저장소의 이미지 태그를 업데이트합니다.
5. `ArgoCD(아르고CD, GitOps 배포 도구)`가 변경을 감지해 `Kubernetes(쿠버네티스)`에 자동 반영합니다.
6. 운영 관측은 `ELK(로그)` + `Prometheus/Grafana(메트릭)`로 확인합니다.

즉, **코드 변경 -> 자동 검증 -> 자동 이미지 배포 -> 자동 클러스터 반영 -> 모니터링**의 닫힌 루프를 학습하는 것이 핵심입니다.

---

## 2. 왜 이 순서로 진행하나요? (문제 -> 해결)

### 문제
- 개발/테스트/배포/운영이 분리되면 릴리즈가 느려지고 실수 확률이 높습니다.
- 수동 배포는 재현성이 낮고, 장애가 나면 원인 추적이 어렵습니다.

### 해결
- `CI`로 **코드 품질 게이트(빌드/테스트 통과)**를 강제합니다.
- `CD`로 **배포 자동화와 일관성**을 확보합니다.
- `GitOps(Git 기반 운영)`로 **무엇이 배포됐는지** 추적 가능하게 만듭니다.
- 로그/메트릭 수집으로 **운영 피드백 루프**를 완성합니다.

---

## 3. 가장 먼저 할 일 (Step 0)

학습을 시작할 때는 기술 구현 전에 기준선을 먼저 고정해야 합니다.

### 3.1 학습 목표를 문장으로 확정
- 목표 예시:
  - "코드 Push 후 Jenkins가 테스트를 통과한 경우에만 DockerHub에 이미지를 올린다."
  - "ArgoCD가 Manifest 변경을 감지해 Kubernetes를 자동 동기화한다."
  - "배포 후 /actuator, 로그, 대시보드로 정상 동작을 검증한다."

이유:
- 목표가 없으면 도구 설치만 하고 끝나기 쉽습니다.
- 목표를 먼저 고정하면 각 단계의 성공/실패를 명확히 판단할 수 있습니다.

### 3.2 브랜치/저장소 전략 확정
- 최소 2개 저장소를 권장합니다.
  1. `source repo` (애플리케이션 코드)
  2. `manifests repo` (쿠버네티스 배포 파일)

이유:
- GitOps에서는 "실행 코드"와 "배포 선언"을 분리해야 변경 책임이 명확해집니다.
- ArgoCD는 선언 저장소를 기준으로 클러스터 상태를 맞춥니다.

---

## 4. 학습 적용 로드맵 (권장 순서)

### Step 1. 로컬 실행 안정화 (현재 상태 점검)
- 백엔드 테스트: `./gradlew test`
- 프론트 빌드: `npm run build` (`frontend/`)
- 로컬 환경 파일 정리: `application-local.yml`, `.env`

왜 먼저?
- CI/CD는 "자동화"이므로, 수동 실행이 불안정하면 자동화도 실패합니다.

완료 기준:
- 로컬에서 빌드/테스트/실행이 재현 가능

---

### Step 2. 컨테이너 이미지 표준화
- 백엔드 `Dockerfile` 작성/정리
- 프론트 `Dockerfile` 작성/정리
- `docker build` 및 `docker run`으로 동작 검증

왜 필요?
- Kubernetes와 CD는 컨테이너 이미지를 배포 단위로 사용합니다.
- 로컬/테스트/운영 실행 환경을 동일하게 맞출 수 있습니다.

완료 기준:
- 백엔드/프론트 이미지가 각각 정상 실행

---

### Step 3. Docker Compose로 로컬 통합 실행
- 앱 + DB(+ 선택: Elasticsearch/Prometheus) 구성
- `docker compose up -d`로 통합 실행

왜 필요?
- 분리된 서비스 간 네트워크/환경변수/의존성 문제를 초기에 검증할 수 있습니다.
- Kubernetes 전 단계 실습으로 가장 효율적입니다.

완료 기준:
- `compose` 한 번으로 전체 스택이 재시작 가능

---

### Step 4. Jenkins CI 파이프라인 구축
- Webhook 연동
- 파이프라인 최소 단계:
  1. Checkout
  2. Build
  3. Test
  4. Docker Build
  5. Docker Push

왜 필요?
- CI는 "신뢰 가능한 코드만 배포로 보내는 관문"입니다.
- 실패를 빠르게 감지해 비용을 줄입니다.

완료 기준:
- Push 시 Jenkins가 자동 실행되고, 성공 시 이미지가 생성/업로드

---

### Step 5. Kubernetes 기본 배포
- `Deployment`, `Service`, `Ingress` 매니페스트 작성
- `Probe(Liveness/Readiness, 생존/준비 상태 점검)` 설정
- `ConfigMap/Secret` 분리

왜 필요?
- 무중단 운영을 위해서는 파드 개별 상태가 아니라 "서비스 가용성" 중심으로 관리해야 합니다.
- 환경별 설정 분리와 헬스체크는 실무 필수입니다.

완료 기준:
- 클러스터에서 앱 접근 가능, 재시작/롤링업데이트 동작 확인

---

### Step 6. ArgoCD CD 파이프라인 완성 (GitOps)
- ArgoCD 설치
- Manifest Repo 등록
- `Sync Policy Automated` 적용
- Jenkins에서 이미지 태그 변경 커밋 -> ArgoCD 자동 배포

왜 필요?
- Jenkins가 클러스터를 직접 건드리지 않고, Git을 통해 배포 이력을 남길 수 있습니다.
- "누가/언제/무엇을 배포"했는지 추적성이 크게 향상됩니다.

완료 기준:
- 이미지 태그 변경 커밋만으로 자동 배포 완료

---

### Step 7. 모니터링/로깅 연결
- `Prometheus + Grafana`: 메트릭 관측
- `ELK`: 로그 수집/검색/분석
- 기본 대시보드: HTTP 요청, JVM 메모리, 에러율, 응답시간

왜 필요?
- 배포 자동화만으로는 부족하고, 운영 관측이 있어야 개선이 가능합니다.
- 장애 시 "감"이 아니라 데이터로 대응할 수 있습니다.

완료 기준:
- 배포 후 메트릭과 로그에서 정상/이상 패턴 확인 가능

---

## 5. 지금 당장 시작할 첫 액션

처음 시작은 아래 3가지만 하시면 됩니다.

1. `source repo`와 `manifests repo`를 분리할지 결정
2. Jenkins/DockerHub/GitHub 자격증명 목록 정리
3. 현재 코드 기준 `Step 1(로컬 안정화)` 체크리스트 실행

체크리스트:
- [ ] `./gradlew test` 성공
- [ ] `frontend npm run build` 성공
- [ ] 환경변수 파일 템플릿 준비 (`application-local.yml.example`, `.env.example`)

---

## 6. 실무 장단점 (학습 관점)

장점:
- 배포 속도와 일관성이 크게 향상됩니다.
- 실패 지점을 조기에 발견합니다.
- 운영 데이터 기반으로 개선이 가능합니다.

단점:
- 초기 설정량이 많고 도구 학습 비용이 큽니다.
- 파이프라인이 복잡해지면 유지보수 부담이 증가합니다.

학습 팁:
- 한 번에 완성하려 하지 말고, **Step 1 -> Step 2 -> ...** 순서로 작게 성공을 쌓으세요.

---

## 7. 다음 문서 예고

다음 단계 문서는 아래 순서로 분리하면 학습이 쉽습니다.

1. `01-local-baseline.md` (로컬 안정화)
2. `02-docker-compose.md` (컨테이너 통합)
3. `03-jenkins-ci.md` (CI)
4. `04-k8s-manifests.md` (쿠버네티스 배포)
5. `05-argocd-cd.md` (GitOps CD)
6. `06-observability.md` (ELK + Prometheus + Grafana)

