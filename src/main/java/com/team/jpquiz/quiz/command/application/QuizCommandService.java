package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.quiz.command.infrastructure.QuizCommandMapper;
import com.team.jpquiz.quiz.dto.request.StartQuizRequest;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizCommandService {

    private final QuizCommandMapper quizCommandMapper;

    public QuizAttemptResponse startQuiz(Long userId, StartQuizRequest request) {
        validateInput(userId, request);

        int count = request.getCount();
        int totalQuestions = quizCommandMapper.countAllQuestions();
        if (totalQuestions < count) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        List<Long> questionIds = quizCommandMapper.findRandomQuestionIds(count);
        if (questionIds.size() != count) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("totalQuestions", count);
        quizCommandMapper.insertQuizAttempt(params);

        Object generatedAttemptId = params.get("attemptId");
        if (!(generatedAttemptId instanceof Number)) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
        Long attemptId = ((Number) generatedAttemptId).longValue();

        for (int i = 0; i < questionIds.size(); i++) {
            Long questionId = questionIds.get(i);
            String choiceOrder = quizCommandMapper.findChoiceOrderCsv(questionId);

            int inserted = quizCommandMapper.insertQuizAttemptQuestion(
                    attemptId,
                    i + 1,
                    questionId,
                    choiceOrder
            );

            if (inserted != 1) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }
        }

        return QuizAttemptResponse.builder()
                .attemptId(attemptId)
                .totalQuestions(count)
                .build();
    }

    private void validateInput(Long userId, StartQuizRequest request) {
        if (userId == null || userId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (request == null || request.getCount() == null || request.getCount() < 1 || request.getCount() > 10) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}
