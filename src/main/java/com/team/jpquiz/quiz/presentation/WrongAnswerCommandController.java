package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.quiz.command.application.WrongAnswerCommandService;
import com.team.jpquiz.quiz.command.application.WrongAnswerReviewCommandService;
import com.team.jpquiz.quiz.dto.request.WrongAnswerSaveRequest;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz/wrong-answers")
@RequiredArgsConstructor
// 오답노트 저장/삭제를 처리하는 커맨드 API입니다.
public class WrongAnswerCommandController {

  private final WrongAnswerCommandService wrongAnswerCommandService;
  private final WrongAnswerReviewCommandService wrongAnswerReviewCommandService;

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

  // 오답노트 재학습 세트 생성 (10문제 고정, 부족분 랜덤 보충)
  @PostMapping("/review-set")
  public ApiResponse<QuizAttemptResponse> createReviewSet() {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();

    QuizAttemptResponse response = wrongAnswerReviewCommandService.createReviewSet(currentMemberId);
    return ApiResponse.ok(response);
  }
}
