package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.quiz.command.infrastructure.QuizCommandMapper;
import com.team.jpquiz.quiz.dto.request.QuizSubmitRequest;
import com.team.jpquiz.quiz.dto.request.StartQuizRequest;
import com.team.jpquiz.quiz.dto.response.QuizAnswerResultResponse;
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

    public QuizAnswerResultResponse submitAnswer(Long userId, Long attemptId, QuizSubmitRequest request) {
        validateSubmitInput(userId, attemptId, request);

        Map<String, Object> attemptQuestion = quizCommandMapper.findAttemptQuestionForSubmit(attemptId, request.getSeq());
        if (attemptQuestion == null) {
            int attemptCount = quizCommandMapper.countAttemptById(attemptId);
            if (attemptCount == 0) {
                throw new CustomException(ErrorCode.ATTEMPT_NOT_FOUND);
            }
            throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
        }

        Long ownerId = castToLong(attemptQuestion.get("userId"));
        Long questionId = castToLong(attemptQuestion.get("questionId"));
        Integer totalQuestions = castToInteger(attemptQuestion.get("totalQuestions"));

        if (ownerId == null || questionId == null || totalQuestions == null) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
        if (!ownerId.equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Integer correctFlag = quizCommandMapper.findChoiceCorrectFlag(questionId, request.getChoiceId());
        if (correctFlag == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        boolean correct = correctFlag == 1;

        int submittedCount = quizCommandMapper.countSubmittedAnswer(attemptId, request.getSeq());
        if (submittedCount > 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        int inserted = quizCommandMapper.insertQuizAttemptAnswer(
                attemptId,
                request.getSeq(),
                questionId,
                request.getChoiceId(),
                correct
        );
        if (inserted != 1) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }

        int solvedCount = quizCommandMapper.countSolvedQuestions(attemptId);

        return QuizAnswerResultResponse.builder()
                .attemptId(attemptId)
                .seq(request.getSeq())
                .selectedChoiceId(request.getChoiceId())
                .correct(correct)
                .solvedCount(solvedCount)
                .totalQuestions(totalQuestions)
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

    private void validateSubmitInput(Long userId, Long attemptId, QuizSubmitRequest request) {
        if (userId == null || userId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (attemptId == null || attemptId <= 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        if (request == null || request.getSeq() == null || request.getSeq() < 1 || request.getChoiceId() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private Long castToLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    private Integer castToInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }
}
