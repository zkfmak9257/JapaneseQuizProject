package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.quiz.command.domain.Favorite;
import com.team.jpquiz.quiz.command.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteCommandService {

  private final FavoriteRepository favoriteRepository;

  public void saveFavorite(Long memberId, Long questionId) {
    if (favoriteRepository.existsByMemberIdAndQuestionId(memberId, questionId)) {
      return; // 멱등 처리
    }

    favoriteRepository.save(
        Favorite.builder()
            .memberId(memberId)
            .questionId(questionId)
            .build()
    );
  }

  public void deleteFavorite(Long memberId, Long questionId) {
    favoriteRepository.deleteByMemberIdAndQuestionId(memberId, questionId);
  }
}
