package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.quiz.command.domain.Favorite;
import com.team.jpquiz.quiz.command.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
// 즐겨찾기 등록/해제 도메인 로직을 처리하는 서비스입니다.
public class FavoriteCommandService {

  private final FavoriteRepository favoriteRepository;

  // 중복 등록을 방지하면서 즐겨찾기를 저장합니다.
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

  // 즐겨찾기 항목을 삭제합니다.
  public void deleteFavorite(Long memberId, Long questionId) {
    favoriteRepository.deleteByMemberIdAndQuestionId(memberId, questionId);
  }
}
