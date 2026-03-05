package com.team.jpquiz.quiz.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "답안 제출 및 채점 결과")
public class QuizAnswerResultResponse {

    @Schema(description = "퀴즈 시도 ID", example = "1001")
    private Long attemptId;
    @Schema(description = "문항 순번", example = "1")
    private int seq;
    @Schema(description = "선택한 보기 ID", example = "12")
    private Long selectedChoiceId;
    @Schema(description = "정답 여부", example = "true")
    private boolean correct;
    @Schema(description = "현재까지 풀이한 문제 수", example = "1")
    private int solvedCount;
    @Schema(description = "총 문제 수", example = "10")
    private int totalQuestions;
    @Schema(description = "채점 피드백 메시지", example = "정답입니다!")
    private String feedbackMessage;
    @Schema(description = "단계별 피드백 정보(정답/해설/보기/문장 토큰)")
    private StagePayload stagePayload;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "단계별 피드백 payload")
    public static class StagePayload {
        @Schema(description = "정답 정보")
        private CorrectPayload correct;
        @Schema(description = "해설 정보")
        private ExplanationPayload explanation;
        @Builder.Default
        @Schema(description = "보기별 피드백 목록")
        private List<ChoicePayload> choices = new ArrayList<>();
        @Schema(description = "문장형 문제 피드백")
        private SentencePayload sentence;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정답 텍스트 정보")
    public static class CorrectPayload {
        @Schema(description = "정답 일본어 텍스트", example = "りんご")
        private String jpText;
        @Schema(description = "정답 한국어 의미", example = "사과")
        private String koMeaning;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "해설 정보")
    public static class ExplanationPayload {
        @Schema(description = "한 줄 해설", example = "문맥상 과일을 의미합니다.")
        private String oneLiner;
        @Schema(description = "상세 해설")
        private String detail;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "보기 피드백 정보")
    public static class ChoicePayload {
        @Schema(description = "보기 ID", example = "12")
        private Long choiceId;
        @Schema(description = "보기 일본어 텍스트")
        private String jpText;
        @Schema(description = "보기 한국어 의미")
        private String koMeaning;
        @Schema(description = "보기별 보조 코멘트")
        private String note;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "문장형 문제 피드백")
    public static class SentencePayload {
        @Builder.Default
        @Schema(description = "정답 토큰 상세 목록")
        private List<TokenDetail> correctTokens = new ArrayList<>();
        @Schema(description = "정답 문장(일본어)")
        private String correctTextJp;
        @Schema(description = "오답 힌트")
        private String diffHint;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "토큰 상세 정보")
    public static class TokenDetail {
        @Schema(description = "토큰 텍스트", example = "私")
        private String tokenText;
        @Schema(description = "토큰 한국어 의미", example = "저")
        private String meaningKo;
        @Schema(description = "문법 역할", example = "주어")
        private String grammarRole;
    }
}
