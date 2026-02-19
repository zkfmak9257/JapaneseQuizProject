package com.team.jpquiz.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSentenceTokenResponse {

    private Long tokenId;
    private String tokenText;
    private Integer order;
}
