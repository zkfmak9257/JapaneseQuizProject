# Quiz History

## 목표 / 범위
- 퀴즈(quiz) 도메인 기능 구현 기록
- 통계(stats) 도메인은 범위에서 제외

## 개발 순서 (목표)
1. 문제/보기 조회 (Read)
- 테이블: `quiz_questions`, `quiz_choices`, `quiz_scenes`
- 이유: 데이터 구조 이해 + 화면/API 확인이 가장 쉬움

2. 퀴즈 시작 (세트 생성)
- 테이블: `quiz_attempts`, `quiz_attempt_questions`
- 이유: 이후 모든 제출/결과가 `attempt_id` 기준으로 동작

3. 답안 제출/채점
- 테이블: `quiz_attempt_answers`, `quiz_choices`
- 이유: 가장 복잡한 로직이라 앞 단계가 완성돼야 안정적

4. 퀴즈 완료 처리
- 테이블: `quiz_attempts`
- 이유: 10문제 완료 시 상태 전환, 결과 조회의 기준

5. 결과 조회
- 테이블: `quiz_attempts`, `quiz_attempt_questions`, `quiz_attempt_answers`
- 이유: 제출 데이터 기반으로 화면에 보여줄 결과 정리

## 결정사항
- 1단계 Read API는 `attempt` 기반 조회로 확정
- 사이드프로젝트 범위에서는 attempt 소유자 권한 검증은 제외

## API / DTO 설계 기록
- [CONFIRMED] 1. 문제/보기 조회 (Read, Attempt 기반)
  - Endpoint: `GET /api/quiz/attempts/{attemptId}/questions/{seq}`
  - Path Variables:
    - `attemptId`: 퀴즈 시도 ID
    - `seq`: 현재 문제 번호(1부터 시작)
  - 동작 규칙:
    - 해당 `attemptId`의 `seq` 문제 1개를 반환
    - 보기 순서는 `quiz_attempt_questions.choice_order` 기준으로 고정 반환 (QUIZ-A03/A04)
    - 제출 전 조회에서는 정답/해설/정오답 필드를 반환하지 않음 (QUIZ-A05)
  - Response (ApiResponse):
    - `data.attemptId`
    - `data.seq`
    - `data.totalQuestions`
    - `data.questionId`
    - `data.questionText` (실제 회화 문제 문장)
    - `data.scene`:
      - `sceneId`
      - `name`
      - `description`
    - `data.choices[]`:
      - `choiceId`
      - `choiceText`
      - `order`
    - 제외 필드:
      - `isCorrect`
      - `correctAnswer`
      - `explanation`
  - Error Cases:
    - `404 ATTEMPT_NOT_FOUND`: attempt 없음
    - `404 QUESTION_NOT_FOUND`: 해당 seq 문제 없음
  - 참고:
    - 본 프로젝트 범위에서는 `403 FORBIDDEN (attempt 소유자 검증)`은 이번 단계에서 적용하지 않음
- [CONFIRMED] 1-1. DTO 설계 (Attempt 기반 조회 응답)
  - `QuizAttemptQuestionResponse`
    - `attemptId`, `seq`, `totalQuestions`
    - `questionId`, `questionText`
    - `scene: QuizSceneResponse`
    - `choices: List<QuizChoiceResponse>`
  - `QuizSceneResponse`
    - `sceneId`, `name`, `description`
  - `QuizChoiceResponse`
    - `questionId` (내부 조립용, `@JsonIgnore`)
    - `choiceId`, `choiceText`, `order`
  - 설계 의도:
    - API 스펙 필드를 1:1로 반영해서 Controller/Service에서 변환 로직 최소화
    - `choices.order` 필드로 `choice_order` 고정 반환 요구사항(QUIZ-A03/A04) 대응
    - `isCorrect/correctAnswer/explanation` 필드를 DTO에서 제외해 QUIZ-A05를 구조적으로 보장

## DB 매핑 메모
- [CONFIRMED] 1-2. MyBatis Mapper/쿼리 설계 (Attempt 기반 조회)
  - Mapper
    - `QuizMapper#findAttemptQuestion(attemptId, seq)`
      - 반환: `QuizAttemptQuestionResponse` (문제 본문 + scene + totalQuestions)
    - `QuizMapper#findAttemptQuestionChoices(attemptId, seq)`
      - 반환: `List<QuizChoiceResponse>` (보기 목록)
  - SQL 설계 포인트
    - 문제 본문 조회:
      - `quiz_attempt_questions` + `quiz_questions` + `quiz_scenes` 조인
      - `attempt_id`, `seq`로 1문제 조회
      - `total_questions`는 같은 attempt의 문제 수를 COUNT 서브쿼리로 계산
    - 보기 조회:
      - `quiz_attempt_questions` + `quiz_choices` 조인
      - `attempt_id`, `seq`로 현재 문제의 보기 목록 조회
      - `order`는 `FIND_IN_SET(choice_id, choice_order)`로 계산
  - 정렬 규칙 (QUIZ-A03/A04)
    - 기본: `choice_order` 기준 오름차순
    - 예외: `choice_order`가 비어 있거나, `FIND_IN_SET(...) = 0`인 보기는 뒤(999)로 보냄
    - 마지막 보조 정렬: `choice_id ASC`
  - 데이터 전제
    - `quiz_attempt_questions.choice_order` 형식은 CSV 문자열
    - 예시: `"1002,1001,1004,1003"`
  - 보안/노출 규칙 (QUIZ-A05)
    - Read 조회 SQL에는 정답/해설/정오답 컬럼을 포함하지 않음

## 구현 체크리스트
- [ ] 1. 문제/보기 조회 (Read)
- [ ] 2. 퀴즈 시작 (세트 생성)
- [ ] 3. 답안 제출/채점
- [ ] 4. 퀴즈 완료 처리
- [ ] 5. 결과 조회

## 이슈 / 배운 점
- 아직 없음
