USE jpquiz;

-- JPQuiz 샘플 데이터 (40문제)
-- WORD: 한국어 문제 -> 일본어 보기 선택

INSERT INTO quiz_scenes (scene_id, name, description)
VALUES
  (1, '일상회화', '일상에서 자주 쓰는 일본어 표현'),
  (2, '비즈니스', '회사/업무 상황 일본어 표현'),
  (3, '여행', '공항/교통/숙소 관련 일본어 표현'),
  (4, '식당', '주문/결제 관련 일본어 표현'),
  (5, '쇼핑', '매장/계산 상황 일본어 표현'),
  (6, '병원', '진료/증상 관련 일본어 표현'),
  (7, '학교', '수업/과제 관련 일본어 표현'),
  (8, '긴급상황', '위급 상황 일본어 표현')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  description = VALUES(description);

DROP TEMPORARY TABLE IF EXISTS tmp_seed_40;
CREATE TEMPORARY TABLE tmp_seed_40 (
  question_id BIGINT NOT NULL,
  scene_id BIGINT NOT NULL,
  question_text VARCHAR(255) NOT NULL,
  correct_jp VARCHAR(255) NOT NULL,
  PRIMARY KEY (question_id)
);

INSERT INTO tmp_seed_40 (question_id, scene_id, question_text, correct_jp) VALUES
  (1001, 1, '아침에 처음 만난 동료에게 인사한다.', 'おはようございます'),
  (1002, 1, '감사 인사를 하고 싶다.', 'ありがとうございます'),
  (1003, 1, '처음 만난 사람에게 자신을 소개하며 잘 부탁한다고 말한다.', 'はじめまして'),
  (1004, 1, '상대의 이름을 다시 물어보고 싶다.', 'お名前は何ですか'),
  (1005, 1, '잠시 기다려 달라고 말한다.', '待ってください'),

  (1006, 2, '회의 시작 전에 잘 부탁드립니다라고 말한다.', 'よろしくお願いいたします'),
  (1007, 2, '메일을 받았다고 공손하게 답한다.', '確認しました'),
  (1008, 2, '회의 일정 변경을 요청한다.', '日程を変更していただけますか'),
  (1009, 2, '잠시 자리를 비운다고 말한다.', '少々席を外します'),
  (1010, 2, '업무 자료를 보내 달라고 요청한다.', '資料を送ってください'),

  (1011, 3, '공항에서 탑승구가 어디인지 묻는다.', '搭乗口はどこですか'),
  (1012, 3, '역에서 이 열차가 도쿄행인지 묻는다.', 'この電車は東京行きですか'),
  (1013, 3, '호텔 체크인을 하고 싶다.', 'チェックインをお願いします'),
  (1014, 3, '길을 잃어서 역까지 가는 방법을 묻는다.', '駅までどうやって行きますか'),
  (1015, 3, '택시 기사에게 목적지를 알려준다.', 'この住所までお願いします'),

  (1016, 4, '메뉴판을 요청한다.', 'メニューをください'),
  (1017, 4, '물 한 잔을 부탁한다.', 'お水をください'),
  (1018, 4, '추천 메뉴를 물어본다.', 'おすすめは何ですか'),
  (1019, 4, '계산을 요청한다.', 'お会計をお願いします'),
  (1020, 4, '맵지 않게 해 달라고 부탁한다.', '辛くしないでください'),

  (1021, 5, '이 상품 가격을 묻는다.', 'これはいくらですか'),
  (1022, 5, '사이즈가 있는지 물어본다.', 'このサイズはありますか'),
  (1023, 5, '카드 결제가 가능한지 물어본다.', 'カードで払えますか'),
  (1024, 5, '교환 가능한지 묻는다.', '交換できますか'),
  (1025, 5, '포장을 부탁한다.', '包装をお願いします'),

  (1026, 6, '두통이 있다고 말한다.', '頭が痛いです'),
  (1027, 6, '예약했는지 물어보는 질문에 예약했다고 답한다.', '予約しました'),
  (1028, 6, '어디가 아픈지 묻는다.', 'どこが痛みますか'),
  (1029, 6, '약국에서 하루 세 번 복용 방법을 묻는다.', '一日何回飲めばいいですか'),
  (1030, 6, '알레르기가 있다고 알린다.', 'アレルギーがあります'),

  (1031, 7, '수업이 몇 시에 시작하는지 묻는다.', '授業は何時に始まりますか'),
  (1032, 7, '과제 제출 기한을 묻는다.', '締め切りはいつですか'),
  (1033, 7, '질문이 있다고 말한다.', '質問があります'),
  (1034, 7, '지각해서 죄송하다고 말한다.', '遅れてすみません'),
  (1035, 7, '발표를 시작하겠다고 말한다.', '発表を始めます'),

  (1036, 8, '도와 달라고 외친다.', '助けてください'),
  (1037, 8, '경찰을 불러 달라고 요청한다.', '警察を呼んでください'),
  (1038, 8, '119에 전화해 달라고 요청한다.', '119番に電話してください'),
  (1039, 8, '지갑을 잃어버렸다고 말한다.', '財布をなくしました'),
  (1040, 8, '길을 잃었다고 말한다.', '道に迷いました');

INSERT INTO quiz_questions (question_id, scene_id, question_type, question_text)
SELECT question_id, scene_id, 'WORD', question_text
FROM tmp_seed_40
ON DUPLICATE KEY UPDATE
  scene_id = VALUES(scene_id),
  question_type = VALUES(question_type),
  question_text = VALUES(question_text);

DELETE qc
FROM quiz_choices qc
JOIN tmp_seed_40 s ON s.question_id = qc.question_id;

INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct)
SELECT
  s.question_id * 10 + n.choice_no AS choice_id,
  s.question_id,
  CASE n.choice_no
    WHEN 1 THEN s.correct_jp
    WHEN 2 THEN d1.correct_jp
    WHEN 3 THEN d2.correct_jp
    ELSE d3.correct_jp
  END AS choice_text,
  CASE WHEN n.choice_no = 1 THEN 1 ELSE 0 END AS is_correct
FROM tmp_seed_40 s
JOIN tmp_seed_40 d1
  ON d1.question_id = (s.question_id - MOD(s.question_id - 1001, 5)) + MOD(MOD(s.question_id - 1001, 5) + 1, 5)
JOIN tmp_seed_40 d2
  ON d2.question_id = (s.question_id - MOD(s.question_id - 1001, 5)) + MOD(MOD(s.question_id - 1001, 5) + 2, 5)
JOIN tmp_seed_40 d3
  ON d3.question_id = (s.question_id - MOD(s.question_id - 1001, 5)) + MOD(MOD(s.question_id - 1001, 5) + 3, 5)
JOIN (
  SELECT 1 AS choice_no
  UNION ALL SELECT 2
  UNION ALL SELECT 3
  UNION ALL SELECT 4
) n;

DROP TEMPORARY TABLE IF EXISTS tmp_seed_40;

-- 확인용
-- SELECT COUNT(*) AS scenes FROM quiz_scenes;
-- SELECT COUNT(*) AS questions_1001_1040 FROM quiz_questions WHERE question_id BETWEEN 1001 AND 1040;
-- SELECT COUNT(*) AS choices_1001_1040 FROM quiz_choices WHERE question_id BETWEEN 1001 AND 1040;
-- SELECT COUNT(*) AS correct_choices_1001_1040 FROM quiz_choices WHERE question_id BETWEEN 1001 AND 1040 AND is_correct = 1;
