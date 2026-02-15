package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import com.team.jpquiz.quiz.query.application.WrongAnswerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz/wrong-answers")
@RequiredArgsConstructor
public class WrongAnswerQueryController {

  private final WrongAnswerQueryService wrongAnswerQueryService;

  @GetMapping
  public ApiResponse<PageResponse<WrongAnswerResponse>> getWrongAnswerList(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();

    PageResponse<WrongAnswerResponse> response = wrongAnswerQueryService.getWrongAnswerList(currentMemberId, page, size);
    return ApiResponse.ok(response);
  }
}
