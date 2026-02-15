package com.team.jpquiz.quiz.command.domain.repository;

import com.team.jpquiz.quiz.command.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  boolean existsByMemberIdAndQuestionId(Long memberId, Long questionId);

  Optional<Favorite> findByMemberIdAndQuestionId(Long memberId, Long questionId);

  void deleteByMemberIdAndQuestionId(Long memberId, Long questionId);
}
