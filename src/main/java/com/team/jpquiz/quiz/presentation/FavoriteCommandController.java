package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.global.security.UserPrincipal;
import com.team.jpquiz.quiz.command.application.FavoriteCommandService;
import com.team.jpquiz.quiz.command.application.FavoriteReviewCommandService;
import com.team.jpquiz.quiz.dto.response.FavoriteToggleResponse;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/favorites", "/api/quiz/favorites"})
@RequiredArgsConstructor
// 즐겨찾기 등록/해제를 처리하는 커맨드 API입니다.
public class FavoriteCommandController {

  private final FavoriteCommandService favoriteCommandService;
  private final FavoriteReviewCommandService favoriteReviewCommandService;

  // 문제를 즐겨찾기에 등록합니다.
  @PostMapping("/{questionId}")
  public ResponseEntity<Void> saveFavorite(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long questionId
  ) {
    favoriteCommandService.saveFavorite(userPrincipal.getUserId(), questionId);
    return ResponseEntity.ok().build();
  }

  // 문제를 즐겨찾기에서 해제합니다.
  @DeleteMapping("/{questionId}")
  public ResponseEntity<Void> deleteFavorite(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long questionId
  ) {
    favoriteCommandService.deleteFavorite(userPrincipal.getUserId(), questionId);
    return ResponseEntity.noContent().build();
  }

  // 즐겨찾기 문제들로 복습 세트를 생성합니다.
  @PostMapping("/review-set")
  public ApiResponse<QuizAttemptResponse> createReviewSet(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestParam(required = false) String category
  ) {
    QuizAttemptResponse response = favoriteReviewCommandService.createReviewSet(
        userPrincipal.getUserId(), category);
    return ApiResponse.ok(response);
  }

  // 문제의 즐겨찾기 상태를 토글합니다.
  @PostMapping("/{questionId}/toggle")
  public ApiResponse<FavoriteToggleResponse> toggleFavorite(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long questionId
  ) {
    FavoriteToggleResponse response = favoriteCommandService.toggleFavorite(userPrincipal.getUserId(), questionId);
    return ApiResponse.ok(response);
  }

}
