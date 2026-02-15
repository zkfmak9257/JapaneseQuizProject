package com.team.jpquiz.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptResponse {
    private Long attemptId;
    private int totalQuestions;


}
