# Quiz History

## 목표 / 범위
- 퀴즈(quiz) 도메인 기능 구현 기록
- 통계(stats) 도메인은 범위에서 제외

## 개발 순서 (목표)
1. 문제/보기 조회 (Read) // 260210 완료
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
- DTO는 Lombok 기반으로 작성하고, `@Setter`는 사용하지 않음

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
  - 구현 규칙 (코드 스타일)
    - Lombok: `@Getter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` 사용
    - `@Setter`는 사용하지 않음 (의도치 않은 값 변경 방지)
    - 리스트 필드(`choices`)는 `@Builder.Default` + 빈 리스트로 초기화
  - 스코프 정리
    - 1단계(Read, Attempt 기반)에서는 `QuizAttemptQuestionResponse`, `QuizSceneResponse`, `QuizChoiceResponse`만 사용
    - 구 스펙(`GET /api/quiz/questions`)용 DTO(`QuizQuestionResponse`, `QuizQuestionsResponse`)는 혼선 방지를 위해 제거
- [CONFIRMED] 2. 퀴즈 시작 API 스펙 (Issue-1)
  - Endpoint: `POST /api/quiz/attempts/start`
  - 인증: 필요 (`401 UNAUTHORIZED` 대상)
  - Request Body:
    - `count` (Integer, 필수): 생성할 문제 수
    - 제약: 최소 1, 최대 10
  - 동작 규칙:
    - 요청 `count`만큼 랜덤 문제를 선택해 1개의 `quiz_attempts`를 생성한다.
    - 선택된 각 문제를 `quiz_attempt_questions`에 `seq`(1부터 시작)로 저장한다.
    - 각 문제의 `choice_order`는 시작 시점에 랜덤으로 고정 저장한다. (QUIZ-A03/A04)
    - 시작 응답에는 정답/해설/정오답 필드를 포함하지 않는다. (QUIZ-A05)
  - Success Response (200, `ApiResponse<QuizAttemptResponse>`):
    - `data.attemptId`: 생성된 시도 ID
    - `data.totalQuestions`: 실제 배정 문제 수
  - Error Cases:
    - `400 INVALID_REQUEST`
      - `count`가 null/범위(1~10) 밖인 경우
      - 요청한 `count`보다 문제 풀이 풀(pool) 개수가 부족한 경우
    - `401 UNAUTHORIZED`
      - 인증 정보 없음/유효하지 않음
  - Out of Scope (Issue-1):
    - attempt 소유자 검증 로직
    - 문제/보기 상세 반환
    - 제출/채점/완료 처리
- [CONFIRMED] 2-1. 퀴즈 시작 DTO 확정 (Issue-2)
  - `StartQuizRequest`
    - 필드: `count` (Integer)
    - 검증: `@NotNull`, `@Min(1)`, `@Max(10)`
    - 의도: 요청 유효성(필수/범위)을 Controller 진입 시점에 빠르게 차단
  - `QuizAttemptResponse`
    - 필드: `attemptId` (Long), `totalQuestions` (int)
    - 의도: 시작 API는 "시도 식별값 + 배정 문제 수"만 반환하고 상세 문제/정답은 포함하지 않음 (QUIZ-A05)
- [CONFIRMED] 3-1. 답안 제출/채점 API 스펙 (Issue-3)
  - Endpoint: `POST /api/quiz/attempts/{attemptId}/answers`
  - 인증: 필요 (`401 UNAUTHORIZED` 대상)
  - Request:
    - Path Variable
      - `attemptId` (Long, 필수): 제출 대상 퀴즈 시도 ID
    - Body
      - `seq` (Integer, 필수): 현재 문제 순번(1부터 시작)
      - `choiceId` (Long, 필수): 사용자가 선택한 보기 ID
  - 동작 규칙:
    - `attemptId + seq`에 해당하는 문제 배정(`quiz_attempt_questions`)이 있어야 제출 가능
    - 제출 `choiceId`는 해당 문제의 보기(`quiz_choices`)에 속해야 함
    - 정답 여부는 서버가 판정해 `quiz_attempt_answers`에 저장
    - 동일 문항 재제출 정책은 3단계 구현 중 확정(현재는 out of scope)
  - Success Response (200, `ApiResponse<QuizAnswerResultResponse>`):
    - `data.attemptId`: 제출 대상 시도 ID
    - `data.seq`: 제출한 문제 순번
    - `data.selectedChoiceId`: 사용자가 제출한 보기 ID
    - `data.correct`: 정답 여부
    - `data.solvedCount`: 현재 attempt 기준 제출 완료 문항 수
    - `data.totalQuestions`: 전체 문항 수
  - Error Cases:
    - `400 INVALID_REQUEST`
      - `seq`, `choiceId` 누락/형식 오류/범위 오류
      - 제출 `choiceId`가 해당 문제 보기에 속하지 않는 경우
    - `401 UNAUTHORIZED`
      - 인증 정보 없음/유효하지 않음
    - `403 FORBIDDEN`
      - 타인 attempt 제출 시도
    - `404 ATTEMPT_NOT_FOUND`
      - attempt 없음
    - `404 QUESTION_NOT_FOUND`
      - 해당 attempt의 `seq` 문제 없음
  - 품질 조건:
    - QUIZ-A03/A04: 시작 시 고정된 문제/보기 집합 기준으로만 제출 허용
    - QUIZ-A05: 제출 응답에서 해설/정답 문구 등 과다 정보는 기본 미노출(정오답 boolean만 반환)
- [DONE] 3-2. 답안 제출/채점 DTO 구현
  - `QuizSubmitRequest`
    - 필드: `seq` (Integer), `choiceId` (Long)
    - 검증: `@NotNull`, `@Min(1)` 적용
  - `QuizAnswerResultResponse`
    - 필드: `attemptId`, `seq`, `selectedChoiceId`, `correct`, `solvedCount`, `totalQuestions`
  - 구현 의도:
    - 요청은 필수값/범위 검증을 DTO에서 1차 차단
    - 응답은 제출 결과 판단에 필요한 최소 필드만 제공
- [CONFIRMED] 4-1. 퀴즈 완료 처리 API 스펙 (Issue-4)
  - Endpoint: `POST /api/quiz/attempts/{attemptId}/complete`
  - 인증: 필요 (`401 UNAUTHORIZED` 대상)
  - Request:
    - Path Variable
      - `attemptId` (Long, 필수): 완료 대상 퀴즈 시도 ID
    - Body
      - 없음
  - 동작 규칙:
    - 요청한 `attemptId`가 존재해야 함
    - 요청 사용자가 해당 attempt 소유자여야 함
    - 완료 시점에는 attempt를 "완료 상태"로 전환하고 완료 시각을 기록함
    - 완료 조건은 "배정된 전체 문항을 모두 제출한 경우"를 기본으로 함
  - Success Response (200, `ApiResponse<QuizCompleteResponse>`):
    - `data.attemptId`: 완료된 시도 ID
    - `data.totalQuestions`: 전체 문항 수
    - `data.solvedCount`: 제출 완료 문항 수
    - `data.completedAt`: 완료 시각
  - Error Cases:
    - `400 INVALID_REQUEST`
      - `attemptId` 형식/범위 오류
      - 미제출 문항이 남은 상태에서 완료 요청한 경우
      - 이미 완료된 attempt를 다시 완료 요청한 경우
    - `401 UNAUTHORIZED`
      - 인증 정보 없음/유효하지 않음
    - `403 FORBIDDEN`
      - 타인 attempt 완료 시도
    - `404 ATTEMPT_NOT_FOUND`
      - attempt 없음
- [DONE] 4-2. 퀴즈 완료 처리 DTO 구현
  - `QuizCompleteResponse`
    - 필드: `attemptId`, `totalQuestions`, `solvedCount`, `completedAt`
  - 구현 의도:
    - 완료 API는 "완료 식별값 + 완료 시점 상태 요약"만 반환
    - 결과 상세(문항별 정오답/해설)는 5단계 결과 조회에서 분리 처리
- [CONFIRMED] 5-1. 결과 조회 API 스펙 (Issue-5)
  - Endpoint: `GET /api/quiz/attempts/{attemptId}/result`
  - 인증: 필요 (`401 UNAUTHORIZED` 대상)
  - Request:
    - Path Variable
      - `attemptId` (Long, 필수): 조회 대상 퀴즈 시도 ID
  - 동작 규칙:
    - 요청한 `attemptId`가 존재해야 함
    - 요청 사용자가 해당 attempt 소유자여야 함
    - 완료된 attempt 기준으로 결과를 반환함
  - Success Response (200, `ApiResponse<QuizResultResponse>`):
    - `data.attemptId`: 시도 ID
    - `data.totalQuestions`: 전체 문항 수
    - `data.solvedCount`: 제출 완료 문항 수
    - `data.correctCount`: 정답 문항 수
    - `data.accuracy`: 정답률(0~100)
    - `data.completedAt`: 완료 시각
  - Error Cases:
    - `400 INVALID_REQUEST`
      - `attemptId` 형식/범위 오류
      - 완료 전 attempt 결과 조회 요청
    - `401 UNAUTHORIZED`
      - 인증 정보 없음/유효하지 않음
    - `403 FORBIDDEN`
      - 타인 attempt 결과 조회 시도
    - `404 ATTEMPT_NOT_FOUND`
      - attempt 없음
- [DONE] 5-2. 결과 조회 DTO 구현
  - `QuizResultResponse`
    - 필드: `attemptId`, `totalQuestions`, `solvedCount`, `correctCount`, `accuracy`, `completedAt`
  - 구현 의도:
    - 결과 조회 API는 요약 통계 중심으로 반환
    - 문항별 상세 결과는 필요 시 후속 DTO로 분리 확장

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
- [CONFIRMED] 2-2. MyBatis Mapper/쿼리 설계 (Issue-3, 퀴즈 시작)
  - Mapper: `QuizCommandMapper`
    - `countAllQuestions()`: 문제 pool 전체 수 조회
    - `findRandomQuestionIds(count)`: 시작 시점 랜덤 문제 ID 추출
    - `findChoiceOrderCsv(questionId)`: 문제별 보기 순서 CSV 생성
    - `insertQuizAttempt(params)`: `quiz_attempts` 1건 저장 + 생성 PK 회수
    - `insertQuizAttemptQuestion(...)`: `quiz_attempt_questions` 배정 행 저장
  - SQL 설계 포인트:
    - 문제 추출은 `ORDER BY RAND() LIMIT #{count}`로 구현
    - `choice_order`는 `GROUP_CONCAT(choice_id ORDER BY RAND())`로 생성해 시작 시점에 고정 저장
    - `insertQuizAttempt`는 MyBatis `useGeneratedKeys`로 `attempt_id`를 회수
  - 품질 조건 반영:
    - QUIZ-A03/A04: 보기 순서를 시작 시점에 DB(`choice_order`)에 확정 저장
    - QUIZ-A05: 시작 단계 SQL은 정답/해설/정오답 정보를 조회/반환하지 않음
- [DONE] 3-3. MyBatis Mapper/쿼리 구현 (Issue-3, 답안 제출/채점)
  - Mapper: `QuizCommandMapper`
    - `countAttemptById(attemptId)`: attempt 존재 여부 확인
    - `findAttemptQuestionForSubmit(attemptId, seq)`: 제출 대상 문제/소유자/총문항 조회
    - `findChoiceCorrectFlag(questionId, choiceId)`: 선택 보기의 정답 여부 조회
    - `insertQuizAttemptAnswer(...)`: 채점 결과 저장
    - `countSubmittedAnswer(attemptId, seq)`: 동일 문항 중복 제출 여부 확인
    - `countSolvedQuestions(attemptId)`: 제출 완료 문항 수 집계
  - SQL 구현 포인트:
    - 제출 시점 검증을 위해 attempt + seq 매핑을 선조회
    - 선택한 `choiceId`가 해당 문제에 속하지 않으면 `null`로 판단 가능하게 구성
    - 채점 결과는 `quiz_attempt_answers`에 insert 저장
- [DONE] 4-3. MyBatis Mapper/쿼리 구현 (Issue-4, 퀴즈 완료 처리)
  - Mapper: `QuizCommandMapper`
    - `findAttemptForComplete(attemptId)`: 완료 전 검증용 attempt 정보(소유자/총문항/완료시각) 조회
    - `completeAttempt(attemptId)`: 미완료 attempt를 완료 처리(`completed_at`)로 전환
  - SQL 구현 포인트:
    - 완료 처리 전 `attempt` 존재/상태 검증에 필요한 최소 컬럼만 조회
    - `completeAttempt`는 `completed_at IS NULL` 조건으로 재완료 요청을 DB 레벨에서 차단 가능하게 설계

## Service 설계 메모
- [DONE] 1-3. QuizQueryService 구현 (Attempt 기반 조회 조립)
  - 대상 메서드: `findAttemptQuestion(Long attemptId, int seq)`
  - 처리 순서:
    1. 입력 검증 (`attemptId`, `seq`)
    2. 문제 본문 조회 (`quizMapper.findAttemptQuestion`)
    3. 문제 미존재 시 예외 처리 (`QUESTION_NOT_FOUND`)
    4. 보기 목록 조회 (`quizMapper.findAttemptQuestionChoices`)
    5. 문제 본문 + 보기를 `QuizAttemptQuestionResponse`로 조립 반환
  - 예외 정책:
    - 유효하지 않은 입력값: `INVALID_REQUEST`
    - 해당 순번 문제 없음: `QUESTION_NOT_FOUND`
  - 구현 포인트:
    - DTO는 `@Setter` 미사용 정책이므로 builder 기반 조립
    - Query Service는 조회 전용이므로 `@Transactional(readOnly = true)` 적용
  - 학습 포인트(비유):
    - `attemptId`는 "시험지 번호", `seq`는 "시험지 내 문제 번호"
    - 서비스는 DB에서 조각(문제 본문/보기)을 가져와 "완성된 1문제 화면 데이터"로 조립하는 역할
- [DONE] 2-3. QuizCommandService 구현 (Issue-4, 퀴즈 시작)
  - 대상 메서드: `startQuiz(Long userId, StartQuizRequest request)`
  - 트랜잭션:
    - `@Transactional`로 attempt/attempt_questions 저장을 하나의 단위로 묶음
    - 중간 실패 시 전체 롤백
  - 처리 순서:
    1. 입력 검증 (`userId`, `request.count`)
    2. 문제 pool 수 확인 (`countAllQuestions`)
    3. 랜덤 문제 ID 추출 (`findRandomQuestionIds`)
    4. `quiz_attempts` 저장 + 생성 `attemptId` 회수
    5. 문제별 `choice_order` 생성 후 `quiz_attempt_questions` 저장
    6. `QuizAttemptResponse(attemptId, totalQuestions)` 반환
  - 예외 정책:
    - `INVALID_REQUEST`: 요청 count가 범위를 벗어나거나 pool보다 큰 경우
    - `UNAUTHORIZED`: userId가 없거나 유효하지 않은 경우
    - `INTERNAL_ERROR`: generated key/배정 insert 이상 등 서버 내부 불일치
- [DONE] 3-4. QuizCommandService 구현 (Issue-3, 답안 제출/채점)
  - 대상 메서드: `submitAnswer(Long userId, Long attemptId, QuizSubmitRequest request)`
  - 처리 순서:
    1. 입력 검증 (`userId`, `attemptId`, `seq`, `choiceId`)
    2. 제출 대상 문제 조회 (`findAttemptQuestionForSubmit`)
    3. attempt 미존재/seq 미존재 분기 처리
    4. attempt 소유자 검증 (`ownerId == userId`)
    5. 선택 보기 정답 여부 확인 (`findChoiceCorrectFlag`)
    6. 동일 문항 중복 제출 차단 (`countSubmittedAnswer`)
    7. 채점 결과 저장 (`insertQuizAttemptAnswer`)
    8. 제출 완료 수 집계 (`countSolvedQuestions`)
    9. `QuizAnswerResultResponse` 반환
  - 예외 정책:
    - `UNAUTHORIZED`: 인증 사용자 식별 불가
    - `INVALID_REQUEST`: 입력값 오류, 문제-보기 불일치
    - `FORBIDDEN`: 타인 attempt 제출 시도
    - `ATTEMPT_NOT_FOUND`: attempt 없음
    - `QUESTION_NOT_FOUND`: attempt 내 해당 seq 문제 없음
    - `INVALID_REQUEST`: 이미 제출한 문항 재제출 시도
    - `INTERNAL_ERROR`: 조회값 타입/저장 결과 불일치
- [DONE] 4-4. QuizCommandService 구현 (Issue-4, 퀴즈 완료 처리)
  - 대상 메서드: `completeQuiz(Long userId, Long attemptId)`
  - 처리 순서:
    1. 입력 검증 (`userId`, `attemptId`)
    2. 완료 대상 attempt 조회 (`findAttemptForComplete`)
    3. attempt 존재/소유권/기완료 상태 검증
    4. 제출 완료 문항 수 검증 (`countSolvedQuestions >= totalQuestions`)
    5. 완료 처리 업데이트 (`completeAttempt`)
    6. `QuizCompleteResponse` 반환
  - 예외 정책:
    - `UNAUTHORIZED`: 인증 사용자 식별 불가
    - `INVALID_REQUEST`: 입력값 오류, 미제출 문항 존재, 기완료 재요청
    - `FORBIDDEN`: 타인 attempt 완료 시도
    - `ATTEMPT_NOT_FOUND`: attempt 없음
    - `INTERNAL_ERROR`: 조회값 타입 불일치

## Controller 구현 메모
- [DONE] 1-4. QuizController 구현 (Attempt 기반 Read 엔드포인트 연결)
  - 클래스: `QuizController`
  - 베이스 경로: `@RequestMapping("/api/quiz")`
  - 엔드포인트: `@GetMapping("/attempts/{attemptId}/questions/{seq}")`
  - 메서드: `getAttemptQuestion(@PathVariable Long attemptId, @PathVariable int seq)`
  - 처리 순서:
    1. URL 경로 변수(`attemptId`, `seq`)를 메서드 파라미터로 받음
    2. `quizQueryService.findAttemptQuestion(attemptId, seq)` 호출
    3. 서비스 반환 DTO를 `ApiResponse.ok(...)`로 감싸 응답
  - 응답 규약:
    - 성공: `ApiResponse<QuizAttemptQuestionResponse>`
    - 실패: `GlobalExceptionHandler`가 `ErrorResponse`로 변환
- [DONE] 2-4. QuizController 구현 (Issue-5, 퀴즈 시작 엔드포인트 연결)
  - 엔드포인트: `POST /api/quiz/attempts/start`
  - 메서드: `startQuiz(@Valid @RequestBody StartQuizRequest request)`
  - 처리 순서:
    1. 요청 DTO 검증(`count` 필수/범위)
    2. `quizCommandService.startQuiz(userId, request)` 호출
    3. 결과를 `ApiResponse.ok(...)`로 반환
  - 응답 규약:
    - 성공: `ApiResponse<QuizAttemptResponse>`
    - 실패: `GlobalExceptionHandler` 또는 Security의 401/403 핸들러
  - 참고:
    - JWT 연동 전 단계에서는 임시 `userId`를 사용하며, 인증 연동 시 SecurityContext 기반으로 교체 예정
- [DONE] 4-5. QuizController 구현 (Issue-4, 퀴즈 완료 처리 엔드포인트 연결)
  - 엔드포인트: `POST /api/quiz/attempts/{attemptId}/complete`
  - 메서드: `completeQuiz(@PathVariable Long attemptId)`
  - 처리 순서:
    1. `SecurityUtil.getCurrentMemberId()`로 인증 사용자 식별
    2. `quizCommandService.completeQuiz(userId, attemptId)` 호출
    3. 결과를 `ApiResponse.ok(...)`로 반환
  - 응답 규약:
    - 성공: `ApiResponse<QuizCompleteResponse>`
    - 실패: `GlobalExceptionHandler` 또는 Security의 401/403 핸들러

## 구현 체크리스트
- [x] 1. 문제/보기 조회 (Read)
- [x] 2. 퀴즈 시작 (세트 생성)
- [x] 3. 답안 제출/채점
- [ ] 4. 퀴즈 완료 처리
- [ ] 5. 결과 조회

## 이슈 / 배운 점
- [DONE] 문제/보기 조회 API 구현 및 수동 테스트 완료
- [PASS] QUIZ-A03/A04: `choice_order` 기준으로 `choices` 순서 고정 반환 확인
- [PASS] QUIZ-A05: 제출 전 응답에서 `isCorrect`, `correctAnswer`, `explanation` 미노출(null) 확인
- [DEFER] 페이지네이션/문제 개수 제한: 2단계(퀴즈 시작/세트 생성)에서 정책 확정 및 적용 예정
- [DEFER] `question_type`별 응답 구조 분리: 문제 유형 확장 시점에 타입별 DTO 분리 설계 예정
- [DONE] 2-5. 수동 테스트(200/400/401) 실행 (Issue-6)
  - 사전 복구:
    - 기존 컴파일 블로커(`PageResponse`, JWT/DTO/WrongAnswer 일부 코드 불일치) 정리 후 `compileJava` 성공
    - 실행 블로커(`@EnableJpaAuditing` 중복, MyBatis 스캔/alias 충돌, 로컬 JWT secret 미설정) 정리 후 `bootRun` 성공
    - 로컬 DB에 `quiz_attempts` 테이블이 없어 테스트용 최소 스키마를 생성
  - 검증 결과:
    - `401` (인증 실패): `POST /api/quiz/attempts/start` 무인증 요청 시 `UNAUTHORIZED` 확인
    - `400` (요청 실패): `POST /api/quiz/attempts/start` + `{"count":2}` 요청 시 문제 pool 부족으로 `INVALID_REQUEST` 확인
    - `200` (정상): `POST /api/quiz/attempts/start` + `{"count":1}` 요청 시 `attemptId`, `totalQuestions` 반환 확인
  - 추가 관찰:
    - `{"count":0}`는 현재 Bean Validation 예외가 공통 핸들링되지 않아 `500`으로 응답됨
    - 향후 `MethodArgumentNotValidException` 핸들링 추가 시 기대 응답을 `400`으로 통일 가능
  - 메모:
    - 400/200 검증을 위해 테스트 중 `POST /api/quiz/attempts/start`를 임시 `permitAll`로 열어 확인 후 원복함
