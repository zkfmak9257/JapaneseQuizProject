package com.team.jpquiz.quiz.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StartQuizRequest {

    @Pattern(regexp = "^(WORD|SENTENCE)$", message = "questionType은 WORD 또는 SENTENCE여야 합니다.")
    private String questionType;

    private Long sceneId;
}
