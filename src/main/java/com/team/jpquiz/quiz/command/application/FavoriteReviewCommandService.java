package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.quiz.command.infrastructure.QuizCommandMapper;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteReviewCommandService {

    private static final int REVIEW_SET_SIZE = 10;

    private final QuizCommandMapper quizCommandMapper;

    public QuizAttemptResponse createReviewSet(Long memberId, String category) {
        validateMember(memberId);

        List<Long> questionIds = quizCommandMapper.findFavoriteQuestionIds(
                memberId, category, REVIEW_SET_SIZE);

        if (questionIds.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return createAttempt(memberId, questionIds);
    }

    private QuizAttemptResponse createAttempt(Long memberId, List<Long> questionIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", memberId);
        params.put("totalQuestions", questionIds.size());
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
                .totalQuestions(questionIds.size())
                .build();
    }

    private void validateMember(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
