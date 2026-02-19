package com.team.jpquiz.quiz.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class QuizSubmitRequest {

    @NotNull(message = "문제 순번(seq)은 필수입니다.")
    @Min(value = 1, message = "문제 순번(seq)은 1 이상이어야 합니다.")
    private Integer seq;

    private Long choiceId;

    // 문장 조합형(SENTENCE) 제출용 토큰 순서
    private List<Long> orderedTokenIds;
}
