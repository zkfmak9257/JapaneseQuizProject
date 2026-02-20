USE jpquiz;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE auth_refresh_tokens;
TRUNCATE TABLE problem_reports;
TRUNCATE TABLE favorites;
TRUNCATE TABLE wrong_answers;
TRUNCATE TABLE quiz_attempt_answers;
TRUNCATE TABLE quiz_attempt_questions;
TRUNCATE TABLE quiz_attempts;
TRUNCATE TABLE quiz_sentence_tokens;
TRUNCATE TABLE quiz_choices;
TRUNCATE TABLE quiz_questions;
TRUNCATE TABLE quiz_scenes;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO quiz_scenes (scene_id, name, description) VALUES
  (1, '일상회화', '일상 대화 상황'),
  (2, '비즈니스', '업무/회의 상황'),
  (3, '여행', '이동/길찾기 상황'),
  (4, '식당', '주문/식사 상황'),
  (5, '쇼핑', '구매/가격 문의 상황'),
  (6, '병원', '진료/증상 설명 상황'),
  (7, '학교', '수업/학습 상황'),
  (8, '긴급상황', '도움 요청 상황');

DROP TEMPORARY TABLE IF EXISTS tmp_word_bank;
CREATE TEMPORARY TABLE tmp_word_bank (
  scene_id INT NOT NULL,
  tmpl_idx INT NOT NULL,
  jp_word VARCHAR(255) NOT NULL,
  ko_word VARCHAR(255) NOT NULL,
  PRIMARY KEY (scene_id, tmpl_idx)
);

INSERT INTO tmp_word_bank (scene_id, tmpl_idx, jp_word, ko_word) VALUES
  (1,1,'おはよう','좋은 아침'),
  (1,2,'ただいま','다녀왔어요'),
  (1,3,'眠い|ねむい','졸리다'),
  (1,4,'忙しい|いそがしい','바쁘다'),
  (1,5,'靴|くつ','신발'),
  (1,6,'食べる|たべる','먹다'),
  (1,7,'飲む|のむ','마시다'),
  (1,8,'暑い|あつい','덥다'),
  (1,9,'寒い|さむい','춥다'),
  (1,10,'静か|しずか','조용하다'),

  (2,1,'会議|かいぎ','회의'),
  (2,2,'資料|しりょう','자료'),
  (2,3,'締め切り|しめきり','마감'),
  (2,4,'報告する|ほうこくする','보고하다'),
  (2,5,'確認する|かくにんする','확인하다'),
  (2,6,'丁寧|ていねい','정중하다'),
  (2,7,'重要|じゅうよう','중요하다'),
  (2,8,'連絡|れんらく','연락'),
  (2,9,'遅刻する|ちこくする','지각하다'),
  (2,10,'予定|よてい','예정'),

  (3,1,'切符|きっぷ','표'),
  (3,2,'出発する|しゅっぱつする','출발하다'),
  (3,3,'到着する|とうちゃくする','도착하다'),
  (3,4,'乗り換える|のりかえる','갈아타다'),
  (3,5,'地図|ちず','지도'),
  (3,6,'迷う|まよう','길을 잃다'),
  (3,7,'早い|はやい','빠르다'),
  (3,8,'遅い|おそい','느리다'),
  (3,9,'空港|くうこう','공항'),
  (3,10,'旅行|りょこう','여행'),

  (4,1,'注文する|ちゅうもんする','주문하다'),
  (4,2,'お勘定|おかんじょう','계산서'),
  (4,3,'美味しい|おいしい','맛있다'),
  (4,4,'不味い|まずい','맛없다'),
  (4,5,'辛い|からい','맵다'),
  (4,6,'甘い|あまい','달다'),
  (4,7,'混む|こむ','붐비다'),
  (4,8,'待つ|まつ','기다리다'),
  (4,9,'飲み物|のみもの','음료'),
  (4,10,'食べ物|たべもの','음식'),

  (5,1,'値段|ねだん','가격'),
  (5,2,'安い|やすい','싸다'),
  (5,3,'高い|たかい','비싸다'),
  (5,4,'試着する|しちゃくする','입어보다'),
  (5,5,'買う|かう','사다'),
  (5,6,'売る|うる','팔다'),
  (5,7,'サイズ','사이즈'),
  (5,8,'色|いろ','색'),
  (5,9,'在庫|ざいこ','재고'),
  (5,10,'割引|わりびき','할인'),

  (6,1,'病院|びょういん','병원'),
  (6,2,'薬|くすり','약'),
  (6,3,'痛い|いたい','아프다'),
  (6,4,'熱|ねつ','열'),
  (6,5,'咳|せき','기침'),
  (6,6,'診察|しんさつ','진료'),
  (6,7,'測る|はかる','재다'),
  (6,8,'休む|やすむ','쉬다'),
  (6,9,'心配|しんぱい','걱정'),
  (6,10,'大丈夫|だいじょうぶ','괜찮다'),

  (7,1,'学生|がくせい','학생'),
  (7,2,'先生|せんせい','선생님'),
  (7,3,'教室|きょうしつ','교실'),
  (7,4,'宿題|しゅくだい','숙제'),
  (7,5,'勉強する|べんきょうする','공부하다'),
  (7,6,'質問する|しつもんする','질문하다'),
  (7,7,'難しい|むずかしい','어렵다'),
  (7,8,'簡単|かんたん','쉽다'),
  (7,9,'試験|しけん','시험'),
  (7,10,'答え|こたえ','정답'),

  (8,1,'助けて|たすけて','도와주세요'),
  (8,2,'危険|きけん','위험'),
  (8,3,'避難する|ひなんする','대피하다'),
  (8,4,'警察|けいさつ','경찰'),
  (8,5,'救急車|きゅうきゅうしゃ','구급차'),
  (8,6,'電話する|でんわする','전화하다'),
  (8,7,'燃える|もえる','타다'),
  (8,8,'止まる|とまる','멈추다'),
  (8,9,'安全|あんぜん','안전'),
  (8,10,'壊れる|こわれる','고장 나다');

INSERT INTO quiz_questions (question_id, scene_id, question_type, question_text)
SELECT scene_id * 1000 + tmpl_idx, scene_id, 'WORD', ko_word
FROM tmp_word_bank;

INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct)
SELECT
  (wb.scene_id * 1000 + wb.tmpl_idx) * 10 + n.choice_no,
  wb.scene_id * 1000 + wb.tmpl_idx,
  CASE n.choice_no
    WHEN 1 THEN wb.jp_word
    WHEN 2 THEN w1.jp_word
    WHEN 3 THEN w2.jp_word
    ELSE w3.jp_word
  END,
  CASE WHEN n.choice_no = 1 THEN 1 ELSE 0 END
FROM tmp_word_bank wb
JOIN tmp_word_bank w1
  ON w1.scene_id = wb.scene_id
 AND w1.tmpl_idx = MOD(wb.tmpl_idx, 10) + 1
JOIN tmp_word_bank w2
  ON w2.scene_id = wb.scene_id
 AND w2.tmpl_idx = MOD(wb.tmpl_idx + 1, 10) + 1
JOIN tmp_word_bank w3
  ON w3.scene_id = wb.scene_id
 AND w3.tmpl_idx = MOD(wb.tmpl_idx + 2, 10) + 1
JOIN (
  SELECT 1 AS choice_no
  UNION ALL SELECT 2
  UNION ALL SELECT 3
  UNION ALL SELECT 4
) n;

DROP TEMPORARY TABLE IF EXISTS tmp_sentence_templates;
CREATE TEMPORARY TABLE tmp_sentence_templates (
  tmpl_idx INT NOT NULL PRIMARY KEY,
  kr_prompt VARCHAR(255) NOT NULL
);

INSERT INTO tmp_sentence_templates (tmpl_idx, kr_prompt) VALUES
  (1, '이것은 얼마입니까?'),
  (2, '오늘은 학교에 갑니다.'),
  (3, '저는 학생입니다.'),
  (4, '물 좀 주세요.'),
  (5, '역은 어디입니까?'),
  (6, '머리가 아픕니다.'),
  (7, '내일 친구를 만납니다.'),
  (8, '잘 부탁드립니다.'),
  (9, '공항까지 어떻게 가요?'),
  (10, '선생님이 교실에 계십니다.');

INSERT INTO quiz_questions (question_id, scene_id, question_type, question_text)
SELECT s.scene_id * 1000 + 100 + t.tmpl_idx, s.scene_id, 'SENTENCE', t.kr_prompt
FROM quiz_scenes s
JOIN tmp_sentence_templates t;

DROP TEMPORARY TABLE IF EXISTS tmp_sentence_tokens;
CREATE TEMPORARY TABLE tmp_sentence_tokens (
  tmpl_idx INT NOT NULL,
  correct_order INT NOT NULL,
  token_text VARCHAR(255) NOT NULL,
  PRIMARY KEY (tmpl_idx, correct_order)
);

INSERT INTO tmp_sentence_tokens (tmpl_idx, correct_order, token_text) VALUES
  (1,1,'これ'), (1,2,'は'), (1,3,'いくら'), (1,4,'です'), (1,5,'か'),
  (2,1,'今日|きょう'), (2,2,'は'), (2,3,'学校|がっこう'), (2,4,'へ'), (2,5,'いきます'),
  (3,1,'私|わたし'), (3,2,'は'), (3,3,'学生|がくせい'), (3,4,'です'),
  (4,1,'お水|おみず'), (4,2,'を'), (4,3,'ください'),
  (5,1,'駅|えき'), (5,2,'は'), (5,3,'どこ'), (5,4,'です'), (5,5,'か'),
  (6,1,'頭|あたま'), (6,2,'が'), (6,3,'痛い|いたい'), (6,4,'です'),
  (7,1,'明日|あした'), (7,2,'友達|ともだち'), (7,3,'に'), (7,4,'あいます'),
  (8,1,'よろしく'), (8,2,'おねがいします'),
  (9,1,'空港|くうこう'), (9,2,'まで'), (9,3,'どうやって'), (9,4,'いきます'), (9,5,'か'),
  (10,1,'先生|せんせい'), (10,2,'が'), (10,3,'教室|きょうしつ'), (10,4,'に'), (10,5,'います');

INSERT INTO quiz_sentence_tokens (token_id, question_id, token_text, correct_order)
SELECT
  (s.scene_id * 1000 + 100 + t.tmpl_idx) * 10 + t.correct_order,
  s.scene_id * 1000 + 100 + t.tmpl_idx,
  t.token_text,
  t.correct_order
FROM quiz_scenes s
JOIN tmp_sentence_tokens t;

DROP TEMPORARY TABLE IF EXISTS tmp_sentence_distractors;
CREATE TEMPORARY TABLE tmp_sentence_distractors (
  tmpl_idx INT NOT NULL,
  dist_idx INT NOT NULL,
  token_text VARCHAR(255) NOT NULL,
  PRIMARY KEY (tmpl_idx, dist_idx)
);

INSERT INTO tmp_sentence_distractors (tmpl_idx, dist_idx, token_text) VALUES
  (1,1,'どこ'), (1,2,'だれ'),
  (2,1,'きのう'), (2,2,'から'),
  (3,1,'先生|せんせい'), (3,2,'でした'),
  (4,1,'おちゃ'), (4,2,'へ'),
  (5,1,'なに'), (5,2,'だれ'),
  (6,1,'おなか'), (6,2,'ですか'),
  (7,1,'きょう'), (7,2,'を'),
  (8,1,'では'), (8,2,'さようなら'),
  (9,1,'電車|でんしゃ'), (9,2,'から'),
  (10,1,'ともだち'), (10,2,'を');

INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct)
SELECT
  (s.scene_id * 1000 + 100 + d.tmpl_idx) * 100 + d.dist_idx,
  s.scene_id * 1000 + 100 + d.tmpl_idx,
  d.token_text,
  0
FROM quiz_scenes s
JOIN tmp_sentence_distractors d;

DROP TEMPORARY TABLE IF EXISTS tmp_word_bank;
DROP TEMPORARY TABLE IF EXISTS tmp_sentence_templates;
DROP TEMPORARY TABLE IF EXISTS tmp_sentence_tokens;
DROP TEMPORARY TABLE IF EXISTS tmp_sentence_distractors;
