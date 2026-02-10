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
  public ResponseEntity<Void> saveWrongAnswer(@RequestBody WrongAnswerSaveRequest request) {
    // TODO: 추후 인증된 사용자 ID로 변경 필요 (@AuthenticationPrincipal)
    Long currentMemberId = 1L;

    wrongAnswerCommandService.saveWrongAnswer(currentMemberId, request);
    return ResponseEntity.ok().build();
  }

  // 오답 삭제
  @DeleteMapping("/{questionId}")
  public ResponseEntity<Void> deleteWrongAnswer(@PathVariable Long questionId) {
    // TODO: 추후 인증된 사용자 ID로 변경 필요
    Long currentMemberId = 1L;

    wrongAnswerCommandService.deleteWrongAnswer(currentMemberId, questionId);
    return ResponseEntity.noContent().build(); // 204 No Content 반환
  }
}