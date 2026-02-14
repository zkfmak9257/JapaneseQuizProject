package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import com.team.jpquiz.quiz.query.application.WrongAnswerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz/wrong-answers")
@RequiredArgsConstructor
public class WrongAnswerQueryController {

  private final WrongAnswerQueryService wrongAnswerQueryService;

  @GetMapping
  public ApiResponse<PageResponse<WrongAnswerResponse>> getWrongAnswerList(
      // 임시 학습용 사용자 식별 방식입니다.
      // TODO: 추후 @AuthenticationPrincipal 기반 인증 사용자 주입으로 교체합니다.
      @RequestHeader("X-Member-Id") Long currentMemberId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    PageResponse<WrongAnswerResponse> response = wrongAnswerQueryService.getWrongAnswerList(currentMemberId, page, size);
    return ApiResponse.success(response);
  }
}
