# 상세 기능 요구사항 (Feature Requirements)

## 목표 / 범위
- **오답노트**: 틀린 문제를 자동으로 저장하고 재학습할 수 있는 기능
- **즐겨찾기**: 사용자가 중요하다고 생각하는 문제를 별도로 관리하는 기능
- **문제 신고**: 문제의 오류(오타, 오답 등)를 사용자가 신고하고 관리자가 처리하는 기능
- **통계**: 서비스 운영 및 사용자 학습 상태를 파악하기 위한 데이터 집계 기능

## 프로젝트 구조 점검 (2026-02-18 기준)
- `quiz` 도메인은 CQRS(Command Query Responsibility Segregation, 명령-조회 책임 분리) 구조를 유지합니다.
  - Write(쓰기): JPA(Java Persistence API, 자바 영속성 API) 기반 `quiz/command/**`
  - Read(읽기): MyBatis(마이바티스) 기반 `quiz/query/**`, `src/main/resources/mappers/quiz/**`
- `report` 도메인은 신고 등록/처리 흐름을 CQRS 구조로 분리했습니다.
  - Write(쓰기): `report/command/**` (신고 등록, 상태 변경)
  - Read(읽기): `report/query/**`, `src/main/resources/mappers/report/**` (관리자 목록 조회)
- `stats` 도메인은 읽기 전용(Query, 조회) 구조로 구현했습니다.
  - `stats/presentation/StatsController`
  - `stats/query/application/StatsQueryService`
  - `stats/query/infrastructure/StatsMapper` + `src/main/resources/mappers/stats/StatsMapper.xml`
- Controller(컨트롤러)는 Command(명령) / Query(조회) 역할로 분리되어 있습니다.
  - 오답노트: `WrongAnswerCommandController`, `WrongAnswerQueryController`
  - 즐겨찾기: `FavoriteCommandController`, `FavoriteQueryController`
- 인증 사용자 식별은 `@AuthenticationPrincipal` 또는 `SecurityUtil`을 사용합니다.
  - `X-Member-Id` 임시 헤더 및 `memberId=1L` 하드코딩은 제거되었습니다.
- 신고 등록(`POST /api/reports`)은 게스트(Guest, 비회원)도 허용하며, 관리자 신고 API는 `@PreAuthorize("hasRole('ADMIN')")`로 보호됩니다.

## 개발 순서 (목표)
1. **오답노트 & 즐겨찾기 (완료)**
   - 이유: 사용자 학습 데이터 축적 및 퀴즈 채점 흐름 자동화의 기반 기능으로 우선 구현 완료.
2. **문제 신고 (C, 진행중)**
   - 이유: 데이터의 무결성을 위해 사용자 피드백을 받는 창구가 필요함.
3. **통계 (R/Aggregate, 진행중)**
   - 이유: 축적된 데이터를 바탕으로 집계가 이루어지므로 가장 마지막에 구현.
4. **관리자 기능 (Admin, 진행중)**
   - 이유: 신고 처리 및 전체 통계 확인을 위한 관리 도구 구축.

## 구현 체크리스트

### 1. 오답노트 (Wrong Answer Note)
- [x] **quizwr-01**: 틀린 문제 자동 저장 (Upsert 로직)
- [x] **quizwr-02**: 오답 목록 조회 (필터링 및 페이징)
- [x] **quizwr-03**: 오답 재학습 세트 생성 (10문제 고정, 부족분 랜덤 채움)
- [x] **quizwr-04**: 재학습/일반 퀴즈 정답 시 오답노트 자동 제거

### 2. 즐겨찾기 (Favorites)
- [x] **quizfav-01**: 중요 문제 등록 (중복 방지 제약)
- [x] **quizfav-02**: 즐겨찾기 목록 조회 (카테고리 필터링)
- [x] **quizfav-03**: 즐겨찾기 해제 (토글 방식)

### 3. 문제 신고 (Problem Report)
- [x] **quizrep-01**: 오류 신고 제출 (회원/게스트 구분)
- [x] **quizrep-02**: [관리자] 신고 목록 조회 (상태별 필터링)
- [ ] **quizrep-03**: [관리자] 신고 처리 (상태 변경 및 문제 비활성화) - 진행중(상태 변경 구현 완료, 문제 비활성화 미연동)

### 4. 통계 (Statistics)
- [ ] **quizstat-01**: 카테고리별 정답률 (latest/first 기준)
- [ ] **quizstat-02**: 문제별 정답률 및 난이도 지표
- [ ] **quizstat-03**: 가장 많이 틀린 TOP N 추출
- [ ] **quizstat-04**: 유저별 학습량 랭킹
- [ ] **quizstat-05**: 서비스 지표 (DAU, 완료율, 전환율) - 진행중(완료율/정답률/최근 활성 유저 수 구현, 전환율 미구현)
- [x] **quizstat-06**: [마이페이지] 개인 학습 통계 조회

## DB 매핑 메모
- `wrong_answers`: `member_id`, `question_id` (Unique), `wrong_count`, `last_wrong_at`
- `favorites`: `member_id`, `question_id` (Unique), `created_at`
- `problem_reports`: `question_id`, `reporter_id`, `report_type`, `status`, `action`
- `quiz_attempts`: `user_id`, `started_at`, `completed_at`
- `quiz_attempt_answers`: 통계 집계의 기준이 되는 원천 데이터 테이블

## 결정사항
- **통계 기준**: 난이도 측정은 '첫 제출(first)'을 기준으로, 성취도 측정은 '최신 제출(latest)'을 기준으로 함.
- **오답노트 제거**: 재학습 세트뿐만 아니라 일반 퀴즈에서도 해당 문제를 맞히면 오답노트에서 삭제함.

## 이슈 / 배운 점
- (구현 진행 시 기록 예정)

## 진행상황 브리핑 (2026-02-18 기준)

### 1) 오답노트
- **quizwr-01 (틀린 문제 자동 저장 / Upsert)**: `완료`
  - 현재: `QuizCommandService.submitAnswer`에서 오답 제출 시 `WrongAnswerCommandService.saveWrongAnswer`를 자동 호출함.
- **quizwr-02 (오답 목록 조회 / 필터+페이징)**: `완료`
  - 현재: `fromDate`, `keyword`, `category` 필터 + 페이징 조회 구현됨.
- **quizwr-03 (재학습 세트 10문제 생성)**: `완료`
  - 현재: `POST /api/quiz/wrong-answers/review-set` 구현됨.
  - 현재: 오답 부족분은 랜덤 문제로 자동 보충됨.
- **quizwr-04 (정답 시 오답노트 자동 제거)**: `완료`
  - 현재: `QuizCommandService.submitAnswer`에서 정답 제출 시 `deleteWrongAnswer` 자동 호출함.

### 2) 즐겨찾기
- **quizfav-01 (등록 + 중복 방지 제약)**: `완료`
  - 현재: `FavoriteCommandService.saveFavorite` 구현됨.
  - 현재: `favorites(member_id, question_id)` 유니크 제약으로 중복 저장 방지됨.
- **quizfav-02 (목록 조회 + 카테고리 필터링)**: `완료`
  - 현재: `GET /api/favorites?page=&size=&category=` 구현됨.
- **quizfav-03 (해제 + 토글 방식)**: `완료`
  - 현재: `DELETE /api/favorites/{questionId}` 해제 API 구현됨.
  - 현재: `POST /api/favorites/{questionId}/toggle` 토글 API 구현됨.

### 3) 문제 신고
- **quizrep-01 (오류 신고 제출 / 회원·게스트)**: `완료`
  - 현재: `POST /api/reports` 구현됨(게스트/회원 모두 신고 가능).
  - 현재: 신고 대상 `questionId` 존재 검증 로직 구현됨.
- **quizrep-02 ([관리자] 신고 목록 조회 / 상태 필터링)**: `완료`
  - 현재: `GET /api/admin/reports?page=&size=&status=` 구현됨.
  - 현재: `status` 값 정규화/검증 및 페이징 구현됨.
- **quizrep-03 ([관리자] 신고 처리 / 상태 변경 및 문제 비활성화)**: `진행중`
  - 현재: `PATCH /api/admin/reports/{reportId}` 상태 변경 + `action` 기록 구현됨.
  - 미완료: 처리 결과와 문제 비활성화 연동은 미구현.

### 4) 통계
- **quizstat-01**: `미착수`
- **quizstat-02**: `미착수`
- **quizstat-03**: `미착수`
- **quizstat-04**: `미착수`
- **quizstat-05**: `진행중`
  - 현재: `GET /api/admin/stats/overview` 구현됨.
  - 현재: `totalAttempts`, `completedAttempts`, `totalAnswers`, `correctAnswers`, `activeUsers7d`, `completionRate`, `accuracyRate` 제공.
  - 미완료: 전환율(conversion rate) 및 DAU(일간 활성 사용자) 정의 기준 확정/구현 필요.
- **quizstat-06**: `완료`
  - 현재: `GET /api/stats/me` 구현됨.
  - 현재: 개인 시도/완료/정답/최근 7일 풀이/완료율/정답률 통계 제공.

## 다음 한 단계 (Step 1)
- 목표: **문제 신고 마무리 + 통계 확장 + 테스트 보강**
- 작업 범위:
  1. `quizrep-03` 마무리: 신고 처리 시 문제 비활성화 연동 여부 확정 및 구현
  2. `quizstat-05` 보강: DAU/전환율 지표 정의 및 집계 쿼리 반영
  3. `quizstat-01~04` 중 1개 이상 우선 구현(카테고리/문제 단위 통계)
  4. 테스트 보강: 서비스 단위(Unit Test, 단위 테스트) + 통합(Integration Test, 통합 테스트) 추가
- 완료 기준:
  - 신고 처리 완료 시 후속 액션(문제 비활성화 포함)이 일관되게 반영됨
  - 통계 API의 지표 정의와 값 산출 기준이 문서/코드에서 일치함
  - 신규 통계/신고 API에 대한 권한/예외/정상 시나리오 테스트가 존재함
