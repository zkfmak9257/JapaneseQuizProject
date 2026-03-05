package com.team.jpquiz.quiz.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "퀴즈 답안 제출 요청")
public class QuizSubmitRequest {

    @Schema(
            description = "문항 순번(1 이상)",
            example = "1",
            minimum = "1"
    )
    @NotNull(message = "문제 순번(seq)은 필수입니다.")
    @Min(value = 1, message = "문제 순번(seq)은 1 이상이어야 합니다.")
    private Integer seq;

    @Schema(description = "선택한 보기 ID(객관식 문제)", example = "12")
    private Long choiceId;

    // 문장 조합형(SENTENCE) 제출용 토큰 순서
    @Schema(
            description = "문장형 문제에서 사용: 사용자가 정렬한 토큰 ID 목록(객관식 문제에서는 생략 가능)",
            example = "[101, 102, 103]"
    )
    private List<Long> orderedTokenIds;
}
