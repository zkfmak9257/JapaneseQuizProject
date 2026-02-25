USE JPQuiz;

-- 1) 3102: 체크인 부탁합니다 -> チェックインをお願いします
UPDATE quiz_sentence_tokens
SET token_text = 'を'
WHERE question_id = 3102 AND correct_order = 2;

UPDATE quiz_sentence_tokens
SET token_text = 'お願いします', meaning_ko = '부탁합니다', grammar_role = '요청 정중 표현'
WHERE question_id = 3102 AND correct_order = 3;

-- 2) 4106: 계산 부탁합니다 -> お会計をお願いします
UPDATE quiz_sentence_tokens
SET token_text = 'を'
WHERE question_id = 4106 AND correct_order = 2;

UPDATE quiz_sentence_tokens
SET token_text = 'お願いします', meaning_ko = '부탁합니다', grammar_role = '요청 정중 표현'
WHERE question_id = 4106 AND correct_order = 3;

-- 3) 4107: 따로따로 계산해주세요 -> 別々でお願いします
UPDATE quiz_sentence_tokens
SET token_text = 'で', meaning_ko = '~에서/로', grammar_role = '장소·수단 조사'
WHERE question_id = 4107 AND correct_order = 2;

-- 4) 4105: 이것으로 주세요 -> これをください
UPDATE quiz_sentence_tokens
SET token_text = 'を', meaning_ko = '~을/를', grammar_role = '목적격 조사'
WHERE question_id = 4105 AND correct_order = 2;

UPDATE quiz_sentence_tokens
SET token_text = 'ください', meaning_ko = '~해 주세요', grammar_role = '요청 표현'
WHERE question_id = 4105 AND correct_order = 3;

-- 5) 6105: 동전으로 바꿔주세요 -> 小銭に両替してください
UPDATE quiz_sentence_tokens
SET token_text = '両替|りょうがえ', meaning_ko = '환전', grammar_role = '명사'
WHERE question_id = 6105 AND correct_order = 3;

UPDATE quiz_sentence_tokens
SET token_text = 'してください', meaning_ko = '~해 주세요', grammar_role = '요청 표현'
WHERE question_id = 6105 AND correct_order = 4;

-- 6) 7106: 도둑이야! -> 泥棒！
UPDATE quiz_sentence_tokens
SET token_text = '！', meaning_ko = '감탄/외침', grammar_role = '문장 부호'
WHERE question_id = 7106 AND correct_order = 2;

-- 7) 5002: 데워주세요 보기 후리가나 보정
UPDATE quiz_choices SET choice_text = '温めてください|あたためてください' WHERE question_id = 5002 AND choice_id = 50021;
UPDATE quiz_choices SET choice_text = '冷やしてください|ひやしてください' WHERE question_id = 5002 AND choice_id = 50022;
UPDATE quiz_choices SET choice_text = '切ってください|きってください' WHERE question_id = 5002 AND choice_id = 50023;
UPDATE quiz_choices SET choice_text = '開けてください|あけてください' WHERE question_id = 5002 AND choice_id = 50024;

-- 8) 2109/4110/4107/2101 토큰 의미 보정(선택적 정리)
UPDATE quiz_sentence_tokens
SET meaning_ko = '부탁', grammar_role = '명사'
WHERE token_text IN ('お願い|おねがい')
  AND (meaning_ko IS NULL OR meaning_ko = '' OR meaning_ko = 'お願い');

UPDATE quiz_sentence_tokens
SET meaning_ko = '합니다', grammar_role = '동사·정중'
WHERE token_text = 'します'
  AND (meaning_ko IS NULL OR meaning_ko = '' OR meaning_ko = 'します');
