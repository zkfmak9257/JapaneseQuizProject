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
- Controller(컨트롤러)는 Command(명령) / Query(조회) 역할로 분리되어 있습니다.
  - 오답노트: `WrongAnswerCommandController`, `WrongAnswerQueryController`
  - 즐겨찾기: `FavoriteCommandController`, `FavoriteQueryController`
- 인증 사용자 식별은 `@AuthenticationPrincipal` 또는 `SecurityUtil`을 사용합니다.
  - `X-Member-Id` 임시 헤더 및 `memberId=1L` 하드코딩은 제거되었습니다.
- 미완료 구조:
  - `stats` 패키지는 DTO(Data Transfer Object, 데이터 전송 객체) / Controller / Service / Mapper 스텁만 존재
  - 문제 신고(`problem report`) 도메인 패키지는 아직 생성 전

## 개발 순서 (목표)
1. **오답노트 & 즐겨찾기 (완료)**
   - 이유: 사용자 학습 데이터 축적 및 퀴즈 채점 흐름 자동화의 기반 기능으로 우선 구현 완료.
2. **문제 신고 (C)**
   - 이유: 데이터의 무결성을 위해 사용자 피드백을 받는 창구가 필요함.
3. **통계 (R/Aggregate)**
   - 이유: 축적된 데이터를 바탕으로 집계가 이루어지므로 가장 마지막에 구현.
4. **관리자 기능 (Admin)**
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
- [ ] **quizrep-01**: 오류 신고 제출 (회원/게스트 구분)
- [ ] **quizrep-02**: [관리자] 신고 목록 조회 (상태별 필터링)
- [ ] **quizrep-03**: [관리자] 신고 처리 (상태 변경 및 문제 비활성화)

### 4. 통계 (Statistics)
- [ ] **quizstat-01**: 카테고리별 정답률 (latest/first 기준)
- [ ] **quizstat-02**: 문제별 정답률 및 난이도 지표
- [ ] **quizstat-03**: 가장 많이 틀린 TOP N 추출
- [ ] **quizstat-04**: 유저별 학습량 랭킹
- [ ] **quizstat-05**: 서비스 지표 (DAU, 완료율, 전환율)
- [ ] **quizstat-06**: [마이페이지] 개인 학습 통계 조회

## DB 매핑 메모
- `wrong_answers`: `member_id`, `question_id` (Unique), `wrong_count`, `last_wrong_at`
- `favorites`: `member_id`, `question_id` (Unique), `created_at`
- `problem_reports`: `question_id`, `reporter_id`, `report_type`, `status`, `action`
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
- **quizrep-01**: `미착수`
- **quizrep-02**: `미착수`
- **quizrep-03**: `미착수`

### 4) 통계
- **quizstat-01**: `미착수`
- **quizstat-02**: `미착수`
- **quizstat-03**: `미착수`
- **quizstat-04**: `미착수`
- **quizstat-05**: `미착수`
- **quizstat-06**: `미착수`

## 다음 한 단계 (Step 1)
- 목표: **문제 신고(Problem Report, 문제 신고) 기능의 최소 동작 단위 구현**
- 작업 범위:
  1. `quizrep-01` 구현: 신고 등록 API(`POST /api/reports`) + 저장 로직 구현
  2. `quizrep-02` 구현: 관리자 신고 목록 조회 API(상태 필터 + 페이징)
  3. `quizrep-03` 구현: 관리자 신고 처리 API(상태 변경, 처리 결과 기록)
  4. 테스트 전략 수립: 서비스 단위(Unit Test, 단위 테스트) + 통합(Integration Test, 통합 테스트) 시나리오 정리
- 완료 기준:
  - 사용자 신고 등록/조회/처리 핵심 흐름이 API 단위로 검증됨
  - 관리자 상태 전환 이력이 재현 가능하게 저장됨
  - 권한(회원/관리자) 분기 동작이 401/403 규칙에 맞게 동작함
