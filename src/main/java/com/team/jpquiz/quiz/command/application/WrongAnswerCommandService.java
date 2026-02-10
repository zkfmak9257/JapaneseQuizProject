package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.quiz.command.domain.WrongAnswer;
import com.team.jpquiz.quiz.command.domain.repository.WrongAnswerRepository;
import com.team.jpquiz.quiz.dto.request.WrongAnswerSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WrongAnswerCommandService {

  private final WrongAnswerRepository wrongAnswerRepository;

  // 오답 저장 (또는 갱신)
  public void saveWrongAnswer(Long memberId, WrongAnswerSaveRequest request) {
    wrongAnswerRepository
        .findByMemberIdAndQuestionId(memberId, request.getQuestionId())
        .ifPresentOrElse(
            WrongAnswer::increaseWrongCount, // 이미 있으면 카운트 증가
            () -> wrongAnswerRepository.save(
                WrongAnswer.builder()
                    .memberId(memberId)
                    .questionId(request.getQuestionId())
                    .build()
            )
        );
  }

  // 오답 삭제
  public void deleteWrongAnswer(Long memberId, Long questionId) {
    wrongAnswerRepository.deleteByMemberIdAndQuestionId(memberId, questionId);
  }
}
