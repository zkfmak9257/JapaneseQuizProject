package com.team.jpquiz.quiz.command.domain.repository;

import com.team.jpquiz.quiz.command.domain.WrongAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WrongAnswerRepository extends JpaRepository<WrongAnswer, Long> {

  // 특정 유저가 특정 문제를 틀린 기록이 있는지 조회(Upsert 로직에서 사용)
  Optional<WrongAnswer> findByMemberIdAndQuestionId(Long memberId, Long questionId);

  // 특정 유저의 오답노트에서 특정 문제 삭제(복습 정답 시 사용)
  void deleteByMemberIdAndQuestionId(Long memberId, Long questionId);

}
