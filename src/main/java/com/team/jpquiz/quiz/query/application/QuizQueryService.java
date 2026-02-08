package com.team.jpquiz.quiz.query.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.quiz.dto.response.QuizAttemptQuestionResponse;
import com.team.jpquiz.quiz.dto.response.QuizChoiceResponse;
import com.team.jpquiz.quiz.query.infrastructure.QuizMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizQueryService {
    private final QuizMapper quizMapper;

    public QuizAttemptQuestionResponse findAttemptQuestion(Long attemptId, int seq) {
        validateInput(attemptId, seq);

        QuizAttemptQuestionResponse question = quizMapper.findAttemptQuestion(attemptId, seq);

        if (question == null) {
            throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
        }

        List<QuizChoiceResponse> choices = quizMapper.findAttemptQuestionChoices(attemptId, seq);

        return QuizAttemptQuestionResponse.builder()
                .attemptId(question.getAttemptId())
                .seq(question.getSeq())
                .totalQuestions(question.getTotalQuestions())
                .questionId(question.getQuestionId())
                .questionText(question.getQuestionText())
                .scene(question.getScene())
                .choices(choices)
                .build();
    }

    private void validateInput(Long attemptId, int seq) {
        if(attemptId == null || attemptId <= 0 || seq < 1) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

}
