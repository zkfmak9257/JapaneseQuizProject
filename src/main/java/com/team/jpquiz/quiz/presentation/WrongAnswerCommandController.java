package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.quiz.command.application.WrongAnswerCommandService;
import com.team.jpquiz.quiz.dto.request.WrongAnswerSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz/wrong-answers")
@RequiredArgsConstructor
public class WrongAnswerCommandController {

  private final WrongAnswerCommandService wrongAnswerCommandService;

  // 오답 수동 저장 (테스트용)
  @PostMapping
  public ResponseEntity<Void> saveWrongAnswer(
      // 임시 학습용 사용자 식별 방식입니다.
      // TODO: 추후 @AuthenticationPrincipal 기반 인증 사용자 주입으로 교체합니다.
      @RequestHeader("X-Member-Id") Long currentMemberId,
      @RequestBody WrongAnswerSaveRequest request
  ) {
    wrongAnswerCommandService.saveWrongAnswer(currentMemberId, request);
    return ResponseEntity.ok().build();
  }

  // 오답 삭제
  @DeleteMapping("/{questionId}")
  public ResponseEntity<Void> deleteWrongAnswer(
      // 임시 학습용 사용자 식별 방식입니다.
      // TODO: 추후 @AuthenticationPrincipal 기반 인증 사용자 주입으로 교체합니다.
      @RequestHeader("X-Member-Id") Long currentMemberId,
      @PathVariable Long questionId
  ) {
    wrongAnswerCommandService.deleteWrongAnswer(currentMemberId, questionId);
    return ResponseEntity.noContent().build(); // 204 No Content 반환
  }
}
