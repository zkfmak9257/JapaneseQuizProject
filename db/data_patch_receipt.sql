USE JPQuiz;

-- 여행 일반 맥락에서 領収書를 レシート로 통일
UPDATE quiz_choices
SET choice_text = REPLACE(choice_text, '領収書|りょうしゅうしょ', 'レシート')
WHERE choice_text LIKE '%領収書|りょうしゅうしょ%';

UPDATE quiz_sentence_tokens
SET token_text = REPLACE(token_text, '領収書|りょうしゅうしょ', 'レシート')
WHERE token_text LIKE '%領収書|りょうしゅうしょ%';

UPDATE quiz_sentence_tokens
SET meaning_ko = '영수증',
    grammar_role = '명사'
WHERE token_text = 'レシート';

-- 3014(영수증) 문제는 보기 중복 방지
UPDATE quiz_choices
SET choice_text = '伝票|でんぴょう'
WHERE question_id = 3014
  AND choice_id = 30142;
