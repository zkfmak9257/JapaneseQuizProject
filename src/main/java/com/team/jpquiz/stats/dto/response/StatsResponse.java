package com.team.jpquiz.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StatsResponse {

    private StatsResponse() {
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyStats {
        private Long memberId;
        private int totalAttempts;
        private int completedAttempts;
        private int totalAnswers;
        private int correctAnswers;
        private int recent7dAnswers;
        private double completionRate;
        private double accuracyRate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminOverview {
        private int totalAttempts;
        private int completedAttempts;
        private int totalAnswers;
        private int correctAnswers;
        private int activeUsers7d;
        private double completionRate;
        private double accuracyRate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryAccuracy {
        private String category;
        private int totalAnswers;
        private int correctAnswers;
        private double accuracyRate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionStats {
        private Long questionId;
        private String questionText;
        private String category;
        private int totalAnswers;
        private int correctAnswers;
        private int wrongAnswers;
        private double accuracyRate;
        private double difficultyScore;
        private String difficultyLevel;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopWrongQuestion {
        private Long questionId;
        private String questionText;
        private String category;
        private int totalAnswers;
        private int wrongAnswers;
        private double wrongRate;
    }

}
