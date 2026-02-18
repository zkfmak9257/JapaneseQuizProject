-- Patch old quiz tables to current backend expectations
-- 실행 예시:
--   mysql -h 127.0.0.1 -P 3306 -u jpquiz -p JPQuiz < db/schema_patch_quiz.sql

USE JPQuiz;

-- 1) quiz_choices: add correctness flag used by grading query
ALTER TABLE quiz_choices
  ADD COLUMN IF NOT EXISTS is_correct TINYINT(1) NOT NULL DEFAULT 0;

-- 2) quiz_attempts: guest attempt support + completion column
ALTER TABLE quiz_attempts
  MODIFY COLUMN user_id BIGINT NULL;

ALTER TABLE quiz_attempts
  ADD COLUMN IF NOT EXISTS completed_at DATETIME NULL AFTER started_at;

-- 3) quiz_attempt_questions: question_id should be mandatory
ALTER TABLE quiz_attempt_questions
  MODIFY COLUMN question_id BIGINT NOT NULL;
