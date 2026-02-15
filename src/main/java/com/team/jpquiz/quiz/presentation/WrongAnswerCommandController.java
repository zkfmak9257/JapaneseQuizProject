package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.quiz.command.application.WrongAnswerCommandService;
import com.team.jpquiz.quiz.dto.request.WrongAnswerSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz/wrong-answers")
@RequiredArgsConstructor
public class WrongAnswerCommandController {

  private final WrongAnswerCommandService wrongAnswerCommandService;

  // 오답 수동 저장 (테스트용)
  @PostMapping
  public ApiResponse<Void> saveWrongAnswer(@Valid @RequestBody WrongAnswerSaveRequest request) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();

    wrongAnswerCommandService.saveWrongAnswer(currentMemberId, request);
    return ApiResponse.ok();
  }

  // 오답 삭제
  @DeleteMapping("/{questionId}")
  public ApiResponse<Void> deleteWrongAnswer(@PathVariable Long questionId) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();

    wrongAnswerCommandService.deleteWrongAnswer(currentMemberId, questionId);
    return ApiResponse.ok();
  }
}
