-- JPQuiz 샘플 데이터 (한국어 카테고리 + 일본어 문제/보기)
-- 실행 예시:
--   mysql -h 127.0.0.1 -P 3306 -u jpquiz -p JPQuiz < db/seed.sql

USE JPQuiz;

-- 1) 카테고리/상황
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

-- 2) 문제 + 보기 (총 40문제, 문제당 4보기)
INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1001, 1, '아침에 처음 만난 동료에게 인사한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10011, 1001, 'おはようございます', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10012, 1001, 'こんばんは', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10013, 1001, 'おやすみなさい', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10014, 1001, 'さようなら', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1002, 1, '감사 인사를 하고 싶다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10021, 1002, 'すみません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10022, 1002, 'ありがとうございます', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10023, 1002, 'おめでとうございます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10024, 1002, 'よろしくお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1003, 1, '처음 만난 사람에게 자신을 소개하며 잘 부탁한다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10031, 1003, 'はじめまして', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10032, 1003, 'いってきます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10033, 1003, 'ただいま', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10034, 1003, 'いただきます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1004, 1, '상대의 이름을 다시 물어보고 싶다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10041, 1004, 'お名前は何ですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10042, 1004, '何時ですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10043, 1004, 'どこですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10044, 1004, 'いくらですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1005, 1, '잠시 기다려 달라고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10051, 1005, '急いでください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10052, 1005, '待ってください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10053, 1005, '手伝ってください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10054, 1005, '見てください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1006, 2, '회의 시작 전에 잘 부탁드립니다라고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10061, 1006, 'お疲れ様です', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10062, 1006, '失礼します', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10063, 1006, 'よろしくお願いいたします', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10064, 1006, 'ごめんなさい', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1007, 2, '메일을 받았다고 공손하게 답한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10071, 1007, '確認しました', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10072, 1007, '知りません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10073, 1007, 'できません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10074, 1007, '帰ります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1008, 2, '회의 일정 변경을 요청한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10081, 1008, '日程を変更していただけますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10082, 1008, 'もう終わりました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10083, 1008, '席がありません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10084, 1008, 'ここで食べます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1009, 2, '잠시 자리를 비운다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10091, 1009, '少々席を外します', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10092, 1009, 'ずっと座っています', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10093, 1009, '今すぐ出社します', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10094, 1009, '今日は休みません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1010, 2, '업무 자료를 보내 달라고 요청한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10101, 1010, '資料を送ってください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10102, 1010, '資料は不要です', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10103, 1010, '資料を捨ててください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10104, 1010, '資料が見えません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1011, 3, '공항에서 탑승구가 어디인지 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10111, 1011, '搭乗口はどこですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10112, 1011, '出口は閉まっていますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10113, 1011, '荷物は重いですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10114, 1011, '飛行機は小さいですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1012, 3, '역에서 이 열차가 도쿄행인지 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10121, 1012, 'この電車は東京行きですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10122, 1012, 'この電車は高いですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10123, 1012, 'この電車は新しいですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10124, 1012, 'この電車は空いていますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1013, 3, '호텔 체크인을 하고 싶다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10131, 1013, 'チェックアウトをお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10132, 1013, 'チェックインをお願いします', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10133, 1013, '予約をキャンセルします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10134, 1013, '部屋を掃除します', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1014, 3, '길을 잃어서 역까지 가는 방법을 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10141, 1014, '駅までどうやって行きますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10142, 1014, '駅は何時に開きますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10143, 1014, '駅で何を食べますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10144, 1014, '駅は何階ですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1015, 3, '택시 기사에게 목적지를 알려준다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10151, 1015, 'この住所までお願いします', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10152, 1015, 'この料理をお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10153, 1015, 'この商品をお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10154, 1015, 'この資料をお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1016, 4, '메뉴판을 요청한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10161, 1016, 'メニューをください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10162, 1016, 'お水は結構です', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10163, 1016, 'お会計をお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10164, 1016, '予約しています', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1017, 4, '물 한 잔을 부탁한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10171, 1017, 'お茶を入れます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10172, 1017, 'お水をください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10173, 1017, 'お皿を下げます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10174, 1017, 'お店を出ます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1018, 4, '추천 메뉴를 물어본다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10181, 1018, 'おすすめは何ですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10182, 1018, '人気がありません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10183, 1018, '今日は休みですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10184, 1018, '席はありません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1019, 4, '계산을 요청한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10191, 1019, '注文します', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10192, 1019, 'お会計をお願いします', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10193, 1019, '席を替えます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10194, 1019, '写真を撮ります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1020, 4, '맵지 않게 해 달라고 부탁한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10201, 1020, '辛くしないでください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10202, 1020, 'もっと辛くしてください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10203, 1020, '甘くしないでください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10204, 1020, '大きくしないでください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1021, 5, '이 상품 가격을 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10211, 1021, 'これはいくらですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10212, 1021, 'これはどこですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10213, 1021, 'これは誰ですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10214, 1021, 'これは何時ですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1022, 5, '사이즈가 있는지 물어본다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10221, 1022, '別の色がありますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10222, 1022, 'このサイズはありますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10223, 1022, '返品できますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10224, 1022, '試着できますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1023, 5, '카드 결제가 가능한지 물어본다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10231, 1023, 'カードで払えますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10232, 1023, '現金はありませんか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10233, 1023, 'レシートはいりませんか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10234, 1023, '袋は高いですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1024, 5, '교환 가능한지 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10241, 1024, '交換できますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10242, 1024, '交換しませんか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10243, 1024, '交換しませんでした', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10244, 1024, '交換しませんでしたか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1025, 5, '포장을 부탁한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10251, 1025, '袋をください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10252, 1025, '包装をお願いします', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10253, 1025, '割引をやめてください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10254, 1025, '返品をお願いします', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1026, 6, '두통이 있다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10261, 1026, '熱があります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10262, 1026, '頭が痛いです', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10263, 1026, '喉が渇きました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10264, 1026, '目がきれいです', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1027, 6, '예약했는지 물어보는 질문에 예약했다고 답한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10271, 1027, '予約していません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10272, 1027, '予約しました', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10273, 1027, '保険がありません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10274, 1027, '薬がありません', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1028, 6, '어디가 아픈지 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10281, 1028, 'どこが痛みますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10282, 1028, 'いつ治りますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10283, 1028, 'だれが医者ですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10284, 1028, '何時に終わりますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1029, 6, '약국에서 하루 세 번 복용 방법을 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10291, 1029, '一日何回飲めばいいですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10292, 1029, 'いつ作りましたか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10293, 1029, '何色がいいですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10294, 1029, 'どこで買いましたか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1030, 6, '알레르기가 있다고 알린다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10301, 1030, 'アレルギーがあります', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10302, 1030, '食欲があります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10303, 1030, '元気があります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10304, 1030, '時間があります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1031, 7, '수업이 몇 시에 시작하는지 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10311, 1031, '授業は何時に始まりますか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10312, 1031, '授業はどこで終わりますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10313, 1031, '授業は誰が作りますか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10314, 1031, '授業はいくらですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1032, 7, '과제 제출 기한을 묻는다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10321, 1032, '締め切りはいつですか', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10322, 1032, '答案はどこですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10323, 1032, '先生はだれですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10324, 1032, '教室は何階ですか', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1033, 7, '질문이 있다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10331, 1033, '質問があります', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10332, 1033, '答えがあります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10333, 1033, '間違いがあります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10334, 1033, '問題があります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1034, 7, '지각해서 죄송하다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10341, 1034, '遅れてすみません', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10342, 1034, '早く来ました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10343, 1034, '間に合いました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10344, 1034, '先に帰ります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1035, 7, '발표를 시작하겠다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10351, 1035, '発表を始めます', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10352, 1035, '発表を終わります', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10353, 1035, '発表を聞きます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10354, 1035, '発表を忘れます', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1036, 8, '도와 달라고 외친다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10361, 1036, '助けてください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10362, 1036, '待ってください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10363, 1036, '見てください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10364, 1036, '聞いてください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1037, 8, '경찰을 불러 달라고 요청한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10371, 1037, '警察を呼んでください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10372, 1037, '先生を呼んでください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10373, 1037, '店員を呼んでください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10374, 1037, '友達を呼んでください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1038, 8, '119에 전화해 달라고 요청한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10381, 1038, '119番に電話してください', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10382, 1038, '110番に電話してください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10383, 1038, '会社に電話してください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10384, 1038, '家に電話してください', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1039, 8, '지갑을 잃어버렸다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10391, 1039, '財布をなくしました', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10392, 1039, '鍵を見つけました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10393, 1039, '道を覚えました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10394, 1039, '時間を忘れました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

INSERT INTO quiz_questions (question_id, scene_id, question_text) VALUES (1040, 8, '길을 잃었다고 말한다.') ON DUPLICATE KEY UPDATE scene_id=VALUES(scene_id), question_text=VALUES(question_text);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10401, 1040, '道に迷いました', 1) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10402, 1040, '道を知っています', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10403, 1040, '道を作りました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);
INSERT INTO quiz_choices (choice_id, question_id, choice_text, is_correct) VALUES (10404, 1040, '道を走りました', 0) ON DUPLICATE KEY UPDATE question_id=VALUES(question_id), choice_text=VALUES(choice_text), is_correct=VALUES(is_correct);

-- 확인용
-- SELECT COUNT(*) AS scenes FROM quiz_scenes;
-- SELECT COUNT(*) AS questions FROM quiz_questions;
-- SELECT COUNT(*) AS choices FROM quiz_choices;
-- SELECT COUNT(*) AS correct_choices FROM quiz_choices WHERE is_correct = 1;
