package com.team.jpquiz.quiz.dto.response;

import com.team.jpquiz.quiz.command.domain.WrongAnswer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrongAnswerResponse {

  private Long questionId;
  private int wrongCount;
  private LocalDateTime lastWrongAt;

  // 나중에 문제 내용(content)이나 정답(answer) 같은 필드가 추가될 수 있음
  // 현재는 WrongAnswer 엔티티만으로 만들 수 있는 정보만 포함

  public static WrongAnswerResponse from(WrongAnswer wrongAnswer) {
    return WrongAnswerResponse.builder()
        .questionId(wrongAnswer.getQuestionId())
        .wrongCount(wrongAnswer.getWrongCount())
        .lastWrongAt(wrongAnswer.getLastWrongAt())
        .build();
  }

}
