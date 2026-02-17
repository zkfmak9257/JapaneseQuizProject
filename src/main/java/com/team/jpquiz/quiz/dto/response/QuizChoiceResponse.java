package com.team.jpquiz.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizChoiceResponse {

    @JsonIgnore
    private Long questionId; // 이 보기가 어느 문제 소속인지 연결키
    private Long choiceId; // 보기 Pk
    private String choiceText; // 보기 문장

    // 스펙 요구 choice_order 기준으로 고정 반환
    private Integer order;
}
