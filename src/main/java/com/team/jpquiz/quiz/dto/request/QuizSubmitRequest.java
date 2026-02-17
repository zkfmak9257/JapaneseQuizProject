package com.team.jpquiz.quiz.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizSubmitRequest {

    @NotNull(message = "문제 순번(seq)은 필수입니다.")
    @Min(value = 1, message = "문제 순번(seq)은 1 이상이어야 합니다.")
    private Integer seq;

    @NotNull(message = "선택한 보기 ID(choiceId)는 필수입니다.")
    private Long choiceId;
}
