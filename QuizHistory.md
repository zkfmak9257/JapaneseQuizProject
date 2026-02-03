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
- 아직 없음

## API / DTO 설계 기록
- 아직 없음

## DB 매핑 메모
- 아직 없음

## 구현 체크리스트
- [ ] 1. 문제/보기 조회 (Read)
- [ ] 2. 퀴즈 시작 (세트 생성)
- [ ] 3. 답안 제출/채점
- [ ] 4. 퀴즈 완료 처리
- [ ] 5. 결과 조회

## 이슈 / 배운 점
- 아직 없음
