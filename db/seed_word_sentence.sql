-- JPQuiz 전체 초기화 + 요구사항 맞춤 샘플
-- WORD: 한국어 뜻 -> 일본어 보기 선택
-- SENTENCE: 일본어 토큰 조합(함정 토큰 포함)
-- 실행:
--   mysql -h 127.0.0.1 -P 3306 -u jpquiz -p jpquiz < db/seed_word_sentence.sql

USE JPQuiz;

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

-- WORD 원천: scene별 명사/동사/형용사
-- ko_word를 question_text로 사용(한국어), choice는 일본어
INSERT INTO quiz_questions (question_id, scene_id, question_type, question_text)
SELECT wb.scene_id * 1000 + wb.tmpl_idx, wb.scene_id, 'WORD', wb.ko_word
FROM (
  SELECT 1 AS scene_id, 1 AS tmpl_idx, 'おはよう' AS jp_word, '좋은 아침' AS ko_word
  UNION ALL SELECT 1,2,'ただいま','다녀왔어요'
  UNION ALL SELECT 1,3,'ねむい','졸리다'
  UNION ALL SELECT 1,4,'いそがしい','바쁘다'
  UNION ALL SELECT 1,5,'くつ','신발'
  UNION ALL SELECT 1,6,'たべる','먹다'
  UNION ALL SELECT 1,7,'のむ','마시다'
  UNION ALL SELECT 1,8,'あつい','덥다'
  UNION ALL SELECT 1,9,'さむい','춥다'
  UNION ALL SELECT 1,10,'しずか','조용하다'

  UNION ALL SELECT 2,1,'かいぎ','회의'
  UNION ALL SELECT 2,2,'しりょう','자료'
  UNION ALL SELECT 2,3,'しめきり','마감'
  UNION ALL SELECT 2,4,'ほうこくする','보고하다'
  UNION ALL SELECT 2,5,'かくにんする','확인하다'
  UNION ALL SELECT 2,6,'ていねい','정중하다'
  UNION ALL SELECT 2,7,'じゅうよう','중요하다'
  UNION ALL SELECT 2,8,'れんらく','연락'
  UNION ALL SELECT 2,9,'ちこくする','지각하다'
  UNION ALL SELECT 2,10,'よてい','예정'

  UNION ALL SELECT 3,1,'きっぷ','표'
  UNION ALL SELECT 3,2,'しゅっぱつする','출발하다'
  UNION ALL SELECT 3,3,'とうちゃくする','도착하다'
  UNION ALL SELECT 3,4,'のりかえる','갈아타다'
  UNION ALL SELECT 3,5,'ちず','지도'
  UNION ALL SELECT 3,6,'まよう','길을 잃다'
  UNION ALL SELECT 3,7,'はやい','빠르다'
  UNION ALL SELECT 3,8,'おそい','느리다'
  UNION ALL SELECT 3,9,'くうこう','공항'
  UNION ALL SELECT 3,10,'りょこう','여행'

  UNION ALL SELECT 4,1,'ちゅうもんする','주문하다'
  UNION ALL SELECT 4,2,'おかんじょう','계산서'
  UNION ALL SELECT 4,3,'おいしい','맛있다'
  UNION ALL SELECT 4,4,'まずい','맛없다'
  UNION ALL SELECT 4,5,'からい','맵다'
  UNION ALL SELECT 4,6,'あまい','달다'
  UNION ALL SELECT 4,7,'こむ','붐비다'
  UNION ALL SELECT 4,8,'まつ','기다리다'
  UNION ALL SELECT 4,9,'のみもの','음료'
  UNION ALL SELECT 4,10,'たべもの','음식'

  UNION ALL SELECT 5,1,'ねだん','가격'
  UNION ALL SELECT 5,2,'やすい','싸다'
  UNION ALL SELECT 5,3,'たかい','비싸다'
  UNION ALL SELECT 5,4,'しちゃくする','입어보다'
  UNION ALL SELECT 5,5,'かう','사다'
  UNION ALL SELECT 5,6,'うる','팔다'
  UNION ALL SELECT 5,7,'サイズ','사이즈'
  UNION ALL SELECT 5,8,'いろ','색'
  UNION ALL SELECT 5,9,'ざいこ','재고'
  UNION ALL SELECT 5,10,'わりびき','할인'

  UNION ALL SELECT 6,1,'びょういん','병원'
  UNION ALL SELECT 6,2,'くすり','약'
  UNION ALL SELECT 6,3,'いたい','아프다'
  UNION ALL SELECT 6,4,'ねつ','열'
  UNION ALL SELECT 6,5,'せき','기침'
  UNION ALL SELECT 6,6,'しんさつ','진료'
  UNION ALL SELECT 6,7,'はかる','재다'
  UNION ALL SELECT 6,8,'やすむ','쉬다'
  UNION ALL SELECT 6,9,'しんぱい','걱정'
  UNION ALL SELECT 6,10,'だいじょうぶ','괜찮다'

  UNION ALL SELECT 7,1,'がくせい','학생'
  UNION ALL SELECT 7,2,'せんせい','선생님'
  UNION ALL SELECT 7,3,'きょうしつ','교실'
  UNION ALL SELECT 7,4,'しゅくだい','숙제'
  UNION ALL SELECT 7,5,'べんきょうする','공부하다'
  UNION ALL SELECT 7,6,'しつもんする','질문하다'
  UNION ALL SELECT 7,7,'むずかしい','어렵다'
  UNION ALL SELECT 7,8,'かんたん','쉽다'
  UNION ALL SELECT 7,9,'しけん','시험'
  UNION ALL SELECT 7,10,'こたえ','정답'

  UNION ALL SELECT 8,1,'たすけて','도와주세요'
  UNION ALL SELECT 8,2,'きけん','위험'
  UNION ALL SELECT 8,3,'ひなんする','대피하다'
  UNION ALL SELECT 8,4,'けいさつ','경찰'
  UNION ALL SELECT 8,5,'きゅうきゅうしゃ','구급차'
  UNION ALL SELECT 8,6,'でんわする','전화하다'
  UNION ALL SELECT 8,7,'もえる','타다'
  UNION ALL SELECT 8,8,'とまる','멈추다'
  UNION ALL SELECT 8,9,'あんぜん','안전'
  UNION ALL SELECT 8,10,'こわれる','고장 나다'
) wb;

-- WORD 보기 4개: 정답 + 같은 scene 내 다른 단어 3개
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct)
SELECT
  (q.scene_id * 1000 + q.tmpl_idx) * 10 + n.choice_no,
  (q.scene_id * 1000 + q.tmpl_idx),
  CASE n.choice_no
    WHEN 1 THEN q.jp_word
    WHEN 2 THEN w1.jp_word
    WHEN 3 THEN w2.jp_word
    ELSE w3.jp_word
  END,
  CASE WHEN n.choice_no = 1 THEN 1 ELSE 0 END
FROM (
  SELECT 1 AS scene_id, 1 AS tmpl_idx, 'おはよう' AS jp_word
  UNION ALL SELECT 1,2,'ただいま'
  UNION ALL SELECT 1,3,'ねむい'
  UNION ALL SELECT 1,4,'いそがしい'
  UNION ALL SELECT 1,5,'くつ'
  UNION ALL SELECT 1,6,'たべる'
  UNION ALL SELECT 1,7,'のむ'
  UNION ALL SELECT 1,8,'あつい'
  UNION ALL SELECT 1,9,'さむい'
  UNION ALL SELECT 1,10,'しずか'

  UNION ALL SELECT 2,1,'かいぎ'
  UNION ALL SELECT 2,2,'しりょう'
  UNION ALL SELECT 2,3,'しめきり'
  UNION ALL SELECT 2,4,'ほうこくする'
  UNION ALL SELECT 2,5,'かくにんする'
  UNION ALL SELECT 2,6,'ていねい'
  UNION ALL SELECT 2,7,'じゅうよう'
  UNION ALL SELECT 2,8,'れんらく'
  UNION ALL SELECT 2,9,'ちこくする'
  UNION ALL SELECT 2,10,'よてい'

  UNION ALL SELECT 3,1,'きっぷ'
  UNION ALL SELECT 3,2,'しゅっぱつする'
  UNION ALL SELECT 3,3,'とうちゃくする'
  UNION ALL SELECT 3,4,'のりかえる'
  UNION ALL SELECT 3,5,'ちず'
  UNION ALL SELECT 3,6,'まよう'
  UNION ALL SELECT 3,7,'はやい'
  UNION ALL SELECT 3,8,'おそい'
  UNION ALL SELECT 3,9,'くうこう'
  UNION ALL SELECT 3,10,'りょこう'

  UNION ALL SELECT 4,1,'ちゅうもんする'
  UNION ALL SELECT 4,2,'おかんじょう'
  UNION ALL SELECT 4,3,'おいしい'
  UNION ALL SELECT 4,4,'まずい'
  UNION ALL SELECT 4,5,'からい'
  UNION ALL SELECT 4,6,'あまい'
  UNION ALL SELECT 4,7,'こむ'
  UNION ALL SELECT 4,8,'まつ'
  UNION ALL SELECT 4,9,'のみもの'
  UNION ALL SELECT 4,10,'たべもの'

  UNION ALL SELECT 5,1,'ねだん'
  UNION ALL SELECT 5,2,'やすい'
  UNION ALL SELECT 5,3,'たかい'
  UNION ALL SELECT 5,4,'しちゃくする'
  UNION ALL SELECT 5,5,'かう'
  UNION ALL SELECT 5,6,'うる'
  UNION ALL SELECT 5,7,'サイズ'
  UNION ALL SELECT 5,8,'いろ'
  UNION ALL SELECT 5,9,'ざいこ'
  UNION ALL SELECT 5,10,'わりびき'

  UNION ALL SELECT 6,1,'びょういん'
  UNION ALL SELECT 6,2,'くすり'
  UNION ALL SELECT 6,3,'いたい'
  UNION ALL SELECT 6,4,'ねつ'
  UNION ALL SELECT 6,5,'せき'
  UNION ALL SELECT 6,6,'しんさつ'
  UNION ALL SELECT 6,7,'はかる'
  UNION ALL SELECT 6,8,'やすむ'
  UNION ALL SELECT 6,9,'しんぱい'
  UNION ALL SELECT 6,10,'だいじょうぶ'

  UNION ALL SELECT 7,1,'がくせい'
  UNION ALL SELECT 7,2,'せんせい'
  UNION ALL SELECT 7,3,'きょうしつ'
  UNION ALL SELECT 7,4,'しゅくだい'
  UNION ALL SELECT 7,5,'べんきょうする'
  UNION ALL SELECT 7,6,'しつもんする'
  UNION ALL SELECT 7,7,'むずかしい'
  UNION ALL SELECT 7,8,'かんたん'
  UNION ALL SELECT 7,9,'しけん'
  UNION ALL SELECT 7,10,'こたえ'

  UNION ALL SELECT 8,1,'たすけて'
  UNION ALL SELECT 8,2,'きけん'
  UNION ALL SELECT 8,3,'ひなんする'
  UNION ALL SELECT 8,4,'けいさつ'
  UNION ALL SELECT 8,5,'きゅうきゅうしゃ'
  UNION ALL SELECT 8,6,'でんわする'
  UNION ALL SELECT 8,7,'もえる'
  UNION ALL SELECT 8,8,'とまる'
  UNION ALL SELECT 8,9,'あんぜん'
  UNION ALL SELECT 8,10,'こわれる'
) q
JOIN (
  SELECT 1 AS scene_id, 1 AS tmpl_idx, 'おはよう' AS jp_word
  UNION ALL SELECT 1,2,'ただいま'
  UNION ALL SELECT 1,3,'ねむい'
  UNION ALL SELECT 1,4,'いそがしい'
  UNION ALL SELECT 1,5,'くつ'
  UNION ALL SELECT 1,6,'たべる'
  UNION ALL SELECT 1,7,'のむ'
  UNION ALL SELECT 1,8,'あつい'
  UNION ALL SELECT 1,9,'さむい'
  UNION ALL SELECT 1,10,'しずか'

  UNION ALL SELECT 2,1,'かいぎ'
  UNION ALL SELECT 2,2,'しりょう'
  UNION ALL SELECT 2,3,'しめきり'
  UNION ALL SELECT 2,4,'ほうこくする'
  UNION ALL SELECT 2,5,'かくにんする'
  UNION ALL SELECT 2,6,'ていねい'
  UNION ALL SELECT 2,7,'じゅうよう'
  UNION ALL SELECT 2,8,'れんらく'
  UNION ALL SELECT 2,9,'ちこくする'
  UNION ALL SELECT 2,10,'よてい'

  UNION ALL SELECT 3,1,'きっぷ'
  UNION ALL SELECT 3,2,'しゅっぱつする'
  UNION ALL SELECT 3,3,'とうちゃくする'
  UNION ALL SELECT 3,4,'のりかえる'
  UNION ALL SELECT 3,5,'ちず'
  UNION ALL SELECT 3,6,'まよう'
  UNION ALL SELECT 3,7,'はやい'
  UNION ALL SELECT 3,8,'おそい'
  UNION ALL SELECT 3,9,'くうこう'
  UNION ALL SELECT 3,10,'りょこう'

  UNION ALL SELECT 4,1,'ちゅうもんする'
  UNION ALL SELECT 4,2,'おかんじょう'
  UNION ALL SELECT 4,3,'おいしい'
  UNION ALL SELECT 4,4,'まずい'
  UNION ALL SELECT 4,5,'からい'
  UNION ALL SELECT 4,6,'あまい'
  UNION ALL SELECT 4,7,'こむ'
  UNION ALL SELECT 4,8,'まつ'
  UNION ALL SELECT 4,9,'のみもの'
  UNION ALL SELECT 4,10,'たべもの'

  UNION ALL SELECT 5,1,'ねだん'
  UNION ALL SELECT 5,2,'やすい'
  UNION ALL SELECT 5,3,'たかい'
  UNION ALL SELECT 5,4,'しちゃくする'
  UNION ALL SELECT 5,5,'かう'
  UNION ALL SELECT 5,6,'うる'
  UNION ALL SELECT 5,7,'サイズ'
  UNION ALL SELECT 5,8,'いろ'
  UNION ALL SELECT 5,9,'ざいこ'
  UNION ALL SELECT 5,10,'わりびき'

  UNION ALL SELECT 6,1,'びょういん'
  UNION ALL SELECT 6,2,'くすり'
  UNION ALL SELECT 6,3,'いたい'
  UNION ALL SELECT 6,4,'ねつ'
  UNION ALL SELECT 6,5,'せき'
  UNION ALL SELECT 6,6,'しんさつ'
  UNION ALL SELECT 6,7,'はかる'
  UNION ALL SELECT 6,8,'やすむ'
  UNION ALL SELECT 6,9,'しんぱい'
  UNION ALL SELECT 6,10,'だいじょうぶ'

  UNION ALL SELECT 7,1,'がくせい'
  UNION ALL SELECT 7,2,'せんせい'
  UNION ALL SELECT 7,3,'きょうしつ'
  UNION ALL SELECT 7,4,'しゅくだい'
  UNION ALL SELECT 7,5,'べんきょうする'
  UNION ALL SELECT 7,6,'しつもんする'
  UNION ALL SELECT 7,7,'むずかしい'
  UNION ALL SELECT 7,8,'かんたん'
  UNION ALL SELECT 7,9,'しけん'
  UNION ALL SELECT 7,10,'こたえ'

  UNION ALL SELECT 8,1,'たすけて'
  UNION ALL SELECT 8,2,'きけん'
  UNION ALL SELECT 8,3,'ひなんする'
  UNION ALL SELECT 8,4,'けいさつ'
  UNION ALL SELECT 8,5,'きゅうきゅうしゃ'
  UNION ALL SELECT 8,6,'でんわする'
  UNION ALL SELECT 8,7,'もえる'
  UNION ALL SELECT 8,8,'とまる'
  UNION ALL SELECT 8,9,'あんぜん'
  UNION ALL SELECT 8,10,'こわれる'
) w1 ON w1.scene_id = q.scene_id AND w1.tmpl_idx = CASE WHEN q.tmpl_idx + 1 > 10 THEN q.tmpl_idx - 9 ELSE q.tmpl_idx + 1 END
JOIN (
  SELECT 1 AS scene_id, 1 AS tmpl_idx, 'おはよう' AS jp_word
  UNION ALL SELECT 1,2,'ただいま'
  UNION ALL SELECT 1,3,'ねむい'
  UNION ALL SELECT 1,4,'いそがしい'
  UNION ALL SELECT 1,5,'くつ'
  UNION ALL SELECT 1,6,'たべる'
  UNION ALL SELECT 1,7,'のむ'
  UNION ALL SELECT 1,8,'あつい'
  UNION ALL SELECT 1,9,'さむい'
  UNION ALL SELECT 1,10,'しずか'

  UNION ALL SELECT 2,1,'かいぎ'
  UNION ALL SELECT 2,2,'しりょう'
  UNION ALL SELECT 2,3,'しめきり'
  UNION ALL SELECT 2,4,'ほうこくする'
  UNION ALL SELECT 2,5,'かくにんする'
  UNION ALL SELECT 2,6,'ていねい'
  UNION ALL SELECT 2,7,'じゅうよう'
  UNION ALL SELECT 2,8,'れんらく'
  UNION ALL SELECT 2,9,'ちこくする'
  UNION ALL SELECT 2,10,'よてい'

  UNION ALL SELECT 3,1,'きっぷ'
  UNION ALL SELECT 3,2,'しゅっぱつする'
  UNION ALL SELECT 3,3,'とうちゃくする'
  UNION ALL SELECT 3,4,'のりかえる'
  UNION ALL SELECT 3,5,'ちず'
  UNION ALL SELECT 3,6,'まよう'
  UNION ALL SELECT 3,7,'はやい'
  UNION ALL SELECT 3,8,'おそい'
  UNION ALL SELECT 3,9,'くうこう'
  UNION ALL SELECT 3,10,'りょこう'

  UNION ALL SELECT 4,1,'ちゅうもんする'
  UNION ALL SELECT 4,2,'おかんじょう'
  UNION ALL SELECT 4,3,'おいしい'
  UNION ALL SELECT 4,4,'まずい'
  UNION ALL SELECT 4,5,'からい'
  UNION ALL SELECT 4,6,'あまい'
  UNION ALL SELECT 4,7,'こむ'
  UNION ALL SELECT 4,8,'まつ'
  UNION ALL SELECT 4,9,'のみもの'
  UNION ALL SELECT 4,10,'たべもの'

  UNION ALL SELECT 5,1,'ねだん'
  UNION ALL SELECT 5,2,'やすい'
  UNION ALL SELECT 5,3,'たかい'
  UNION ALL SELECT 5,4,'しちゃくする'
  UNION ALL SELECT 5,5,'かう'
  UNION ALL SELECT 5,6,'うる'
  UNION ALL SELECT 5,7,'サイズ'
  UNION ALL SELECT 5,8,'いろ'
  UNION ALL SELECT 5,9,'ざいこ'
  UNION ALL SELECT 5,10,'わりびき'

  UNION ALL SELECT 6,1,'びょういん'
  UNION ALL SELECT 6,2,'くすり'
  UNION ALL SELECT 6,3,'いたい'
  UNION ALL SELECT 6,4,'ねつ'
  UNION ALL SELECT 6,5,'せき'
  UNION ALL SELECT 6,6,'しんさつ'
  UNION ALL SELECT 6,7,'はかる'
  UNION ALL SELECT 6,8,'やすむ'
  UNION ALL SELECT 6,9,'しんぱい'
  UNION ALL SELECT 6,10,'だいじょうぶ'

  UNION ALL SELECT 7,1,'がくせい'
  UNION ALL SELECT 7,2,'せんせい'
  UNION ALL SELECT 7,3,'きょうしつ'
  UNION ALL SELECT 7,4,'しゅくだい'
  UNION ALL SELECT 7,5,'べんきょうする'
  UNION ALL SELECT 7,6,'しつもんする'
  UNION ALL SELECT 7,7,'むずかしい'
  UNION ALL SELECT 7,8,'かんたん'
  UNION ALL SELECT 7,9,'しけん'
  UNION ALL SELECT 7,10,'こたえ'

  UNION ALL SELECT 8,1,'たすけて'
  UNION ALL SELECT 8,2,'きけん'
  UNION ALL SELECT 8,3,'ひなんする'
  UNION ALL SELECT 8,4,'けいさつ'
  UNION ALL SELECT 8,5,'きゅうきゅうしゃ'
  UNION ALL SELECT 8,6,'でんわする'
  UNION ALL SELECT 8,7,'もえる'
  UNION ALL SELECT 8,8,'とまる'
  UNION ALL SELECT 8,9,'あんぜん'
  UNION ALL SELECT 8,10,'こわれる'
) w2 ON w2.scene_id = q.scene_id AND w2.tmpl_idx = CASE WHEN q.tmpl_idx + 2 > 10 THEN q.tmpl_idx - 8 ELSE q.tmpl_idx + 2 END
JOIN (
  SELECT 1 AS scene_id, 1 AS tmpl_idx, 'おはよう' AS jp_word
  UNION ALL SELECT 1,2,'ただいま'
  UNION ALL SELECT 1,3,'ねむい'
  UNION ALL SELECT 1,4,'いそがしい'
  UNION ALL SELECT 1,5,'くつ'
  UNION ALL SELECT 1,6,'たべる'
  UNION ALL SELECT 1,7,'のむ'
  UNION ALL SELECT 1,8,'あつい'
  UNION ALL SELECT 1,9,'さむい'
  UNION ALL SELECT 1,10,'しずか'

  UNION ALL SELECT 2,1,'かいぎ'
  UNION ALL SELECT 2,2,'しりょう'
  UNION ALL SELECT 2,3,'しめきり'
  UNION ALL SELECT 2,4,'ほうこくする'
  UNION ALL SELECT 2,5,'かくにんする'
  UNION ALL SELECT 2,6,'ていねい'
  UNION ALL SELECT 2,7,'じゅうよう'
  UNION ALL SELECT 2,8,'れんらく'
  UNION ALL SELECT 2,9,'ちこくする'
  UNION ALL SELECT 2,10,'よてい'

  UNION ALL SELECT 3,1,'きっぷ'
  UNION ALL SELECT 3,2,'しゅっぱつする'
  UNION ALL SELECT 3,3,'とうちゃくする'
  UNION ALL SELECT 3,4,'のりかえる'
  UNION ALL SELECT 3,5,'ちず'
  UNION ALL SELECT 3,6,'まよう'
  UNION ALL SELECT 3,7,'はやい'
  UNION ALL SELECT 3,8,'おそい'
  UNION ALL SELECT 3,9,'くうこう'
  UNION ALL SELECT 3,10,'りょこう'

  UNION ALL SELECT 4,1,'ちゅうもんする'
  UNION ALL SELECT 4,2,'おかんじょう'
  UNION ALL SELECT 4,3,'おいしい'
  UNION ALL SELECT 4,4,'まずい'
  UNION ALL SELECT 4,5,'からい'
  UNION ALL SELECT 4,6,'あまい'
  UNION ALL SELECT 4,7,'こむ'
  UNION ALL SELECT 4,8,'まつ'
  UNION ALL SELECT 4,9,'のみもの'
  UNION ALL SELECT 4,10,'たべもの'

  UNION ALL SELECT 5,1,'ねだん'
  UNION ALL SELECT 5,2,'やすい'
  UNION ALL SELECT 5,3,'たかい'
  UNION ALL SELECT 5,4,'しちゃくする'
  UNION ALL SELECT 5,5,'かう'
  UNION ALL SELECT 5,6,'うる'
  UNION ALL SELECT 5,7,'サイズ'
  UNION ALL SELECT 5,8,'いろ'
  UNION ALL SELECT 5,9,'ざいこ'
  UNION ALL SELECT 5,10,'わりびき'

  UNION ALL SELECT 6,1,'びょういん'
  UNION ALL SELECT 6,2,'くすり'
  UNION ALL SELECT 6,3,'いたい'
  UNION ALL SELECT 6,4,'ねつ'
  UNION ALL SELECT 6,5,'せき'
  UNION ALL SELECT 6,6,'しんさつ'
  UNION ALL SELECT 6,7,'はかる'
  UNION ALL SELECT 6,8,'やすむ'
  UNION ALL SELECT 6,9,'しんぱい'
  UNION ALL SELECT 6,10,'だいじょうぶ'

  UNION ALL SELECT 7,1,'がくせい'
  UNION ALL SELECT 7,2,'せんせい'
  UNION ALL SELECT 7,3,'きょうしつ'
  UNION ALL SELECT 7,4,'しゅくだい'
  UNION ALL SELECT 7,5,'べんきょうする'
  UNION ALL SELECT 7,6,'しつもんする'
  UNION ALL SELECT 7,7,'むずかしい'
  UNION ALL SELECT 7,8,'かんたん'
  UNION ALL SELECT 7,9,'しけん'
  UNION ALL SELECT 7,10,'こたえ'

  UNION ALL SELECT 8,1,'たすけて'
  UNION ALL SELECT 8,2,'きけん'
  UNION ALL SELECT 8,3,'ひなんする'
  UNION ALL SELECT 8,4,'けいさつ'
  UNION ALL SELECT 8,5,'きゅうきゅうしゃ'
  UNION ALL SELECT 8,6,'でんわする'
  UNION ALL SELECT 8,7,'もえる'
  UNION ALL SELECT 8,8,'とまる'
  UNION ALL SELECT 8,9,'あんぜん'
  UNION ALL SELECT 8,10,'こわれる'
) w3 ON w3.scene_id = q.scene_id AND w3.tmpl_idx = CASE WHEN q.tmpl_idx + 3 > 10 THEN q.tmpl_idx - 7 ELSE q.tmpl_idx + 3 END
JOIN (SELECT 1 AS choice_no UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) n;

-- SENTENCE 문제(한국어 프롬프트)
INSERT INTO quiz_questions (question_id, scene_id, question_type, question_text)
SELECT s.scene_id * 1000 + 100 + st.tmpl_idx, s.scene_id, 'SENTENCE', st.kr_prompt
FROM quiz_scenes s
JOIN (
  SELECT 1 AS tmpl_idx, '이것은 얼마입니까?' AS kr_prompt
  UNION ALL SELECT 2, '오늘은 학교에 갑니다.'
  UNION ALL SELECT 3, '저는 학생입니다.'
  UNION ALL SELECT 4, '물 좀 주세요.'
  UNION ALL SELECT 5, '역은 어디입니까?'
  UNION ALL SELECT 6, '머리가 아픕니다.'
  UNION ALL SELECT 7, '내일 친구를 만납니다.'
  UNION ALL SELECT 8, '잘 부탁드립니다.'
  UNION ALL SELECT 9, '공항까지 어떻게 가요?'
  UNION ALL SELECT 10, '선생님이 교실에 계십니다.'
) st;

-- SENTENCE 정답 토큰(일본어 분리 토큰)
INSERT INTO quiz_sentence_tokens (token_id, question_id, token_text, correct_order)
SELECT
  (s.scene_id * 1000 + 100 + t.tmpl_idx) * 10 + t.correct_order,
  (s.scene_id * 1000 + 100 + t.tmpl_idx),
  t.token_text,
  t.correct_order
FROM quiz_scenes s
JOIN (
  SELECT 1 AS tmpl_idx, 1 AS correct_order, 'これ' AS token_text
  UNION ALL SELECT 1,2,'は'
  UNION ALL SELECT 1,3,'いくら'
  UNION ALL SELECT 1,4,'です'
  UNION ALL SELECT 1,5,'か'
  UNION ALL SELECT 2,1,'きょう'
  UNION ALL SELECT 2,2,'は'
  UNION ALL SELECT 2,3,'がっこう'
  UNION ALL SELECT 2,4,'へ'
  UNION ALL SELECT 2,5,'いきます'
  UNION ALL SELECT 3,1,'わたし'
  UNION ALL SELECT 3,2,'は'
  UNION ALL SELECT 3,3,'がくせい'
  UNION ALL SELECT 3,4,'です'
  UNION ALL SELECT 4,1,'おみず'
  UNION ALL SELECT 4,2,'を'
  UNION ALL SELECT 4,3,'ください'
  UNION ALL SELECT 5,1,'えき'
  UNION ALL SELECT 5,2,'は'
  UNION ALL SELECT 5,3,'どこ'
  UNION ALL SELECT 5,4,'です'
  UNION ALL SELECT 5,5,'か'
  UNION ALL SELECT 6,1,'あたま'
  UNION ALL SELECT 6,2,'が'
  UNION ALL SELECT 6,3,'いたい'
  UNION ALL SELECT 6,4,'です'
  UNION ALL SELECT 7,1,'あした'
  UNION ALL SELECT 7,2,'ともだち'
  UNION ALL SELECT 7,3,'に'
  UNION ALL SELECT 7,4,'あいます'
  UNION ALL SELECT 8,1,'よろしく'
  UNION ALL SELECT 8,2,'おねがいします'
  UNION ALL SELECT 9,1,'くうこう'
  UNION ALL SELECT 9,2,'まで'
  UNION ALL SELECT 9,3,'どうやって'
  UNION ALL SELECT 9,4,'いきます'
  UNION ALL SELECT 9,5,'か'
  UNION ALL SELECT 10,1,'せんせい'
  UNION ALL SELECT 10,2,'が'
  UNION ALL SELECT 10,3,'きょうしつ'
  UNION ALL SELECT 10,4,'に'
  UNION ALL SELECT 10,5,'います'
) t;

-- SENTENCE 함정 토큰
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct)
SELECT
  (s.scene_id * 1000 + 100 + d.tmpl_idx) * 100 + d.dist_idx,
  (s.scene_id * 1000 + 100 + d.tmpl_idx),
  d.token_text,
  0
FROM quiz_scenes s
JOIN (
  SELECT 1 AS tmpl_idx, 1 AS dist_idx, 'どこ' AS token_text
  UNION ALL SELECT 1,2,'だれ'
  UNION ALL SELECT 2,1,'きのう'
  UNION ALL SELECT 2,2,'から'
  UNION ALL SELECT 3,1,'せんせい'
  UNION ALL SELECT 3,2,'でした'
  UNION ALL SELECT 4,1,'おちゃ'
  UNION ALL SELECT 4,2,'へ'
  UNION ALL SELECT 5,1,'なに'
  UNION ALL SELECT 5,2,'だれ'
  UNION ALL SELECT 6,1,'おなか'
  UNION ALL SELECT 6,2,'ですか'
  UNION ALL SELECT 7,1,'きょう'
  UNION ALL SELECT 7,2,'を'
  UNION ALL SELECT 8,1,'では'
  UNION ALL SELECT 8,2,'さようなら'
  UNION ALL SELECT 9,1,'でんしゃ'
  UNION ALL SELECT 9,2,'から'
  UNION ALL SELECT 10,1,'ともだち'
  UNION ALL SELECT 10,2,'を'
) d;
