package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.global.security.UserPrincipal;
import com.team.jpquiz.quiz.command.application.FavoriteCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
// 즐겨찾기 등록/해제를 처리하는 커맨드 API입니다.
public class FavoriteCommandController {

  private final FavoriteCommandService favoriteCommandService;

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

}
