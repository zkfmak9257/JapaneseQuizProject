package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.quiz.command.application.FavoriteCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
      // 임시 학습용 사용자 식별 방식입니다.
      // TODO: 추후 @AuthenticationPrincipal 기반 인증 사용자 주입으로 교체합니다.
      @RequestHeader("X-Member-Id") Long currentMemberId,
      @PathVariable Long questionId
  ) {
    favoriteCommandService.saveFavorite(currentMemberId, questionId);
    return ResponseEntity.ok().build();
  }

  // 문제를 즐겨찾기에서 해제합니다.
  @DeleteMapping("/{questionId}")
  public ResponseEntity<Void> deleteFavorite(
      // 임시 학습용 사용자 식별 방식입니다.
      // TODO: 추후 @AuthenticationPrincipal 기반 인증 사용자 주입으로 교체합니다.
      @RequestHeader("X-Member-Id") Long currentMemberId,
      @PathVariable Long questionId
  ) {
    favoriteCommandService.deleteFavorite(currentMemberId, questionId);
    return ResponseEntity.noContent().build();
  }

}
