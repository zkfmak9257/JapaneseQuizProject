package com.team.jpquiz.quiz.command.domain.repository;

import com.team.jpquiz.quiz.command.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 즐겨찾기 커맨드용 데이터 접근을 담당하는 JPA 리포지토리입니다.
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  // 회원이 특정 문제를 이미 즐겨찾기 했는지 확인합니다.
  boolean existsByMemberIdAndQuestionId(Long memberId, Long questionId);

  // 회원의 특정 문제 즐겨찾기 항목을 조회합니다.
  Optional<Favorite> findByMemberIdAndQuestionId(Long memberId, Long questionId);

  // 회원의 특정 문제 즐겨찾기를 삭제합니다.
  void deleteByMemberIdAndQuestionId(Long memberId, Long questionId);
}
