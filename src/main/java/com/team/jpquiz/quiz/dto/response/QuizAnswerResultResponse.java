package com.team.jpquiz.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerResultResponse {

    private Long attemptId;
    private int seq;
    private Long selectedChoiceId;
    private boolean correct;
    private int solvedCount;
    private int totalQuestions;
}
