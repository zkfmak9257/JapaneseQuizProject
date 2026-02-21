-- Patch old quiz tables to current backend expectations
-- 실행 예시:
--   mysql -h 127.0.0.1 -P 3306 -u jpquiz -p JPQuiz < db/schema_patch_quiz.sql

USE JPQuiz;

-- 1) quiz_choices: add correctness flag used by grading query
ALTER TABLE quiz_choices
  ADD COLUMN IF NOT EXISTS is_correct TINYINT(1) NOT NULL DEFAULT 0;

-- 1-1) quiz_questions: 문제 타입(WORD/SENTENCE)
ALTER TABLE quiz_questions
  ADD COLUMN IF NOT EXISTS question_type VARCHAR(20) NOT NULL DEFAULT 'WORD' AFTER scene_id;

-- 1-1-1) quiz_questions: 활성/비활성 상태 컬럼
ALTER TABLE quiz_questions
  ADD COLUMN IF NOT EXISTS is_active TINYINT(1) NOT NULL DEFAULT 1 AFTER question_text;

ALTER TABLE quiz_questions
  ADD COLUMN IF NOT EXISTS deactivated_at DATETIME NULL AFTER is_active;

-- 1-2) SENTENCE 문제용 토큰 테이블
CREATE TABLE IF NOT EXISTS quiz_sentence_tokens (
  token_id BIGINT NOT NULL AUTO_INCREMENT,
  question_id BIGINT NOT NULL,
  token_text VARCHAR(255) NOT NULL,
  correct_order INT NOT NULL,
  PRIMARY KEY (token_id),
  KEY idx_sentence_tokens_question_id (question_id),
  UNIQUE KEY uk_sentence_tokens_question_order (question_id, correct_order),
  CONSTRAINT fk_sentence_tokens_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2) quiz_attempts: guest attempt support + completion column
ALTER TABLE quiz_attempts
  MODIFY COLUMN user_id BIGINT NULL;

ALTER TABLE quiz_attempts
  ADD COLUMN IF NOT EXISTS completed_at DATETIME NULL AFTER started_at;

-- 3) quiz_attempt_questions: question_id should be mandatory
ALTER TABLE quiz_attempt_questions
  MODIFY COLUMN question_id BIGINT NOT NULL;

-- 4) 답안 테이블: SENTENCE 조합 답 저장용 컬럼
ALTER TABLE quiz_attempt_answers
  MODIFY COLUMN selected_choice_id BIGINT NULL;

ALTER TABLE quiz_attempt_answers
  ADD COLUMN IF NOT EXISTS submitted_token_order VARCHAR(500) NULL AFTER selected_choice_id;
