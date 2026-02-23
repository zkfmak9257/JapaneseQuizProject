package com.team.jpquiz.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String feedbackMessage;
    private StagePayload stagePayload;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StagePayload {
        private CorrectPayload correct;
        private ExplanationPayload explanation;
        @Builder.Default
        private List<ChoicePayload> choices = new ArrayList<>();
        private SentencePayload sentence;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CorrectPayload {
        private String jpText;
        private String koMeaning;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExplanationPayload {
        private String oneLiner;
        private String detail;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChoicePayload {
        private Long choiceId;
        private String jpText;
        private String koMeaning;
        private String note;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentencePayload {
        @Builder.Default
        private List<Long> correctTokens = new ArrayList<>();
        private String correctTextJp;
        private String diffHint;
    }
}
