package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.global.security.UserPrincipal;
import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import com.team.jpquiz.quiz.query.application.WrongAnswerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz/wrong-answers")
@RequiredArgsConstructor
// 오답노트 목록 조회를 처리하는 쿼리 API입니다.
public class WrongAnswerQueryController {

  private final WrongAnswerQueryService wrongAnswerQueryService;

  // 회원별 오답노트를 페이징으로 조회합니다.
  @GetMapping
  public ApiResponse<PageResponse<WrongAnswerResponse>> getWrongAnswerList(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    PageResponse<WrongAnswerResponse> response =
        wrongAnswerQueryService.getWrongAnswerList(userPrincipal.getUserId(), page, size);
    return ApiResponse.ok(response);
  }
}
