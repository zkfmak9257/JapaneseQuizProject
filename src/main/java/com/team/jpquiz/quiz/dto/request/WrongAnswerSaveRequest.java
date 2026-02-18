package com.team.jpquiz.quiz.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WrongAnswerSaveRequest {

  @NotNull(message = "문제 ID는 필수입니다.")
  private Long questionId;

  public WrongAnswerSaveRequest(Long questionId) {
    this.questionId = questionId;
  }

  public Long getQuestionId() {
    return questionId;
  }
}
