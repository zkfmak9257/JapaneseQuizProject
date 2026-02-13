package com.team.jpquiz.quiz.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StartQuizRequest {
    @Min(value = 1, message = "문제 수는 1 이상이어야 합니다.")
    @Max(value = 20, message = "문제 수는 20 이하여야 합니다.")
    private Integer count;

}
