package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.quiz.command.application.FavoriteCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteCommandController {

  private final FavoriteCommandService favoriteCommandService;

  @PostMapping("/{questionId}")
  public ResponseEntity<Void> saveFavorite(
      @RequestHeader("X-Member-Id") Long currentMemberId,
      @PathVariable Long questionId
  ) {
    favoriteCommandService.saveFavorite(currentMemberId, questionId);
    return ResponseEntity.ok().build();
  }
  @DeleteMapping("/{questionId}")
  public ResponseEntity<Void> deleteFavorite(
      @RequestHeader ("X-Member-Id") Long currentMemberId,
      @PathVariable Long questionId
  ) {
    favoriteCommandService.deleteFavorite(currentMemberId, questionId);
    return ResponseEntity.noContent().build();
  }

}
