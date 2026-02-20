CREATE DATABASE IF NOT EXISTS jpquiz
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE jpquiz;

-- 1) 회원
CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  withdrawn_at DATETIME NULL,
  last_login_at DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id),
  UNIQUE KEY uk_users_email (email),
  UNIQUE KEY uk_users_nickname (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2) 리프레시 토큰
CREATE TABLE IF NOT EXISTS auth_refresh_tokens (
  token_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  refresh_token_hash VARCHAR(255) NOT NULL,
  is_revoked TINYINT(1) NOT NULL DEFAULT 0,
  revoked_at DATETIME NULL,
  expires_at DATETIME NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (token_id),
  KEY idx_refresh_tokens_user_id (user_id),
  KEY idx_refresh_tokens_hash_revoked (refresh_token_hash, is_revoked),
  CONSTRAINT fk_refresh_tokens_user
    FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3) 퀴즈 카테고리/상황
CREATE TABLE IF NOT EXISTS quiz_scenes (
  scene_id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255) NULL,
  PRIMARY KEY (scene_id),
  UNIQUE KEY uk_quiz_scenes_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4) 퀴즈 문제
CREATE TABLE IF NOT EXISTS quiz_questions (
  question_id BIGINT NOT NULL AUTO_INCREMENT,
  scene_id BIGINT NULL,
  question_type VARCHAR(20) NOT NULL DEFAULT 'WORD',
  question_text TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (question_id),
  KEY idx_quiz_questions_scene_id (scene_id),
  CONSTRAINT fk_quiz_questions_scene
    FOREIGN KEY (scene_id) REFERENCES quiz_scenes (scene_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5-1) 문장 조합형 토큰
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

-- 5) 퀴즈 보기
CREATE TABLE IF NOT EXISTS quiz_choices (
  choice_id BIGINT NOT NULL AUTO_INCREMENT,
  question_id BIGINT NOT NULL,
  choice_text VARCHAR(255) NOT NULL,
  is_correct TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (choice_id),
  KEY idx_quiz_choices_question_id (question_id),
  CONSTRAINT fk_quiz_choices_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6) 퀴즈 시도
CREATE TABLE IF NOT EXISTS quiz_attempts (
  attempt_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NULL,
  total_questions INT NOT NULL,
  started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (attempt_id),
  KEY idx_quiz_attempts_user_id (user_id),
  CONSTRAINT fk_quiz_attempts_user
    FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7) 시도별 문제 배정
CREATE TABLE IF NOT EXISTS quiz_attempt_questions (
  id BIGINT NOT NULL AUTO_INCREMENT,
  attempt_id BIGINT NOT NULL,
  seq INT NOT NULL,
  question_id BIGINT NOT NULL,
  choice_order VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_attempt_questions_attempt_seq (attempt_id, seq),
  KEY idx_attempt_questions_question_id (question_id),
  CONSTRAINT fk_attempt_questions_attempt
    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts (attempt_id),
  CONSTRAINT fk_attempt_questions_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8) 시도별 답안
CREATE TABLE IF NOT EXISTS quiz_attempt_answers (
  answer_id BIGINT NOT NULL AUTO_INCREMENT,
  attempt_id BIGINT NOT NULL,
  seq INT NOT NULL,
  question_id BIGINT NOT NULL,
  selected_choice_id BIGINT NULL,
  submitted_token_order VARCHAR(500) NULL,
  is_correct TINYINT(1) NOT NULL,
  answered_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (answer_id),
  UNIQUE KEY uk_attempt_answers_attempt_seq (attempt_id, seq),
  KEY idx_attempt_answers_question_id (question_id),
  KEY idx_attempt_answers_selected_choice_id (selected_choice_id),
  CONSTRAINT fk_attempt_answers_attempt
    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts (attempt_id),
  CONSTRAINT fk_attempt_answers_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id),
  CONSTRAINT fk_attempt_answers_choice
    FOREIGN KEY (selected_choice_id) REFERENCES quiz_choices (choice_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9) 즐겨찾기
CREATE TABLE IF NOT EXISTS favorites (
  id BIGINT NOT NULL AUTO_INCREMENT,
  member_id BIGINT NOT NULL,
  question_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_favorites_member_question (member_id, question_id),
  KEY idx_favorites_question_id (question_id),
  CONSTRAINT fk_favorites_member
    FOREIGN KEY (member_id) REFERENCES users (user_id),
  CONSTRAINT fk_favorites_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10) 오답노트
CREATE TABLE IF NOT EXISTS wrong_answers (
  id BIGINT NOT NULL AUTO_INCREMENT,
  member_id BIGINT NOT NULL,
  question_id BIGINT NOT NULL,
  wrong_count INT NOT NULL DEFAULT 1,
  last_wrong_at DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_wrong_answers_member_question (member_id, question_id),
  KEY idx_wrong_answers_member_created (member_id, created_at),
  KEY idx_wrong_answers_question_id (question_id),
  CONSTRAINT fk_wrong_answers_member
    FOREIGN KEY (member_id) REFERENCES users (user_id),
  CONSTRAINT fk_wrong_answers_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11) 문제 신고
CREATE TABLE IF NOT EXISTS problem_reports (
  report_id BIGINT NOT NULL AUTO_INCREMENT,
  question_id BIGINT NOT NULL,
  reporter_id BIGINT NULL,
  report_type VARCHAR(30) NOT NULL,
  content VARCHAR(1000) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  action VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (report_id),
  KEY idx_problem_reports_status_created (status, created_at),
  KEY idx_problem_reports_question_id (question_id),
  KEY idx_problem_reports_reporter_id (reporter_id),
  CONSTRAINT fk_problem_reports_question
    FOREIGN KEY (question_id) REFERENCES quiz_questions (question_id),
  CONSTRAINT fk_problem_reports_reporter
    FOREIGN KEY (reporter_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
