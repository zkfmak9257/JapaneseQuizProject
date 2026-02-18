package com.team.jpquiz.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCompleteResponse {

    private Long attemptId;
    private int totalQuestions;
    private int solvedCount;
    private LocalDateTime completedAt;
}
