package com.team.jpquiz.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptQuestionResponse {

    // attempt의 몇 번째 문제인지 식별
    private Long attemptId;
    private int seq;
    private int totalQuestions;

    // 실제 문제 정보
    private Long questionId;
    private String questionText;
    private String questionType;

    // 상황(카테고리) 정보
    private QuizSceneResponse scene;

    // 보기 목록 (order 순서대로 채워질 예정)
    @Default
    private List<QuizChoiceResponse> choices = new ArrayList<>();

    // 문장 조합형 문제(SENTENCE)용 토큰 목록
    @Default
    private List<QuizSentenceTokenResponse> sentenceTokens = new ArrayList<>();
}
