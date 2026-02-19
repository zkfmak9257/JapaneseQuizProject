package com.team.jpquiz.quiz.command.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.quiz.command.infrastructure.QuizCommandMapper;
import com.team.jpquiz.quiz.dto.request.QuizSubmitRequest;
import com.team.jpquiz.quiz.dto.request.StartQuizRequest;
import com.team.jpquiz.quiz.dto.request.WrongAnswerSaveRequest;
import com.team.jpquiz.quiz.dto.response.QuizAnswerResultResponse;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
import com.team.jpquiz.quiz.dto.response.QuizCompleteResponse;
import com.team.jpquiz.quiz.dto.response.QuizResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizCommandService {

    private static final int FIXED_QUIZ_QUESTION_COUNT = 10;

    private final QuizCommandMapper quizCommandMapper;
    private final WrongAnswerCommandService wrongAnswerCommandService;

    public QuizAttemptResponse startQuiz(Long userId, StartQuizRequest request) {
        int count = FIXED_QUIZ_QUESTION_COUNT;
        String questionType = normalizeQuestionType(request);
        Long sceneId = normalizeSceneId(request);

        int totalQuestions = quizCommandMapper.countQuestionsByFilter(questionType, sceneId);
        if (totalQuestions < count) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        List<Long> questionIds = quizCommandMapper.findRandomQuestionIdsByFilter(count, questionType, sceneId);
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

    private String normalizeQuestionType(StartQuizRequest request) {
        if (request == null || request.getQuestionType() == null || request.getQuestionType().isBlank()) {
            return null;
        }
        String normalized = request.getQuestionType().trim().toUpperCase();
        if (!"WORD".equals(normalized) && !"SENTENCE".equals(normalized)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return normalized;
    }

    private Long normalizeSceneId(StartQuizRequest request) {
        if (request == null || request.getSceneId() == null) {
            return null;
        }
        if (request.getSceneId() <= 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return request.getSceneId();
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
        String questionType = castToString(attemptQuestion.get("questionType"));
        Integer totalQuestions = castToInteger(attemptQuestion.get("totalQuestions"));

        if (questionId == null || totalQuestions == null) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
        if (!isAllowedOwner(ownerId, userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        EvaluationResult evaluationResult = evaluateAnswer(questionId, questionType, request);
        boolean correct = evaluationResult.correct();

        int submittedCount = quizCommandMapper.countSubmittedAnswer(attemptId, request.getSeq());
        if (submittedCount > 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        int inserted = quizCommandMapper.insertQuizAttemptAnswer(
                attemptId,
                request.getSeq(),
                questionId,
                request.getChoiceId(),
                evaluationResult.submittedTokenOrder(),
                correct
        );
        if (inserted != 1) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }

        if (userId != null) {
            updateWrongAnswerNote(userId, questionId, correct);
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

    public QuizCompleteResponse completeQuiz(Long userId, Long attemptId) {
        validateCompleteInput(userId, attemptId);

        Map<String, Object> attempt = quizCommandMapper.findAttemptForComplete(attemptId);
        if (attempt == null) {
            throw new CustomException(ErrorCode.ATTEMPT_NOT_FOUND);
        }

        Long ownerId = castToLong(attempt.get("userId"));
        Integer totalQuestions = castToInteger(attempt.get("totalQuestions"));
        LocalDateTime completedAt = castToLocalDateTime(attempt.get("completedAt"));

        if (totalQuestions == null) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
        if (!isAllowedOwner(ownerId, userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (completedAt != null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        int solvedCount = quizCommandMapper.countSolvedQuestions(attemptId);
        if (solvedCount < totalQuestions) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        int updated = quizCommandMapper.completeAttempt(attemptId);
        if (updated != 1) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        return QuizCompleteResponse.builder()
                .attemptId(attemptId)
                .totalQuestions(totalQuestions)
                .solvedCount(solvedCount)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public QuizResultResponse getQuizResult(Long userId, Long attemptId) {
        validateResultInput(userId, attemptId);

        Map<String, Object> attempt = quizCommandMapper.findAttemptForResult(attemptId);
        if (attempt == null) {
            throw new CustomException(ErrorCode.ATTEMPT_NOT_FOUND);
        }

        Long ownerId = castToLong(attempt.get("userId"));
        Integer totalQuestions = castToInteger(attempt.get("totalQuestions"));
        LocalDateTime completedAt = castToLocalDateTime(attempt.get("completedAt"));

        if (totalQuestions == null) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
        if (!isAllowedOwner(ownerId, userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (completedAt == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        int solvedCount = quizCommandMapper.countSolvedQuestions(attemptId);
        int correctCount = quizCommandMapper.countCorrectAnswers(attemptId);
        int accuracy = totalQuestions > 0 ? (correctCount * 100) / totalQuestions : 0;

        return QuizResultResponse.builder()
                .attemptId(attemptId)
                .totalQuestions(totalQuestions)
                .solvedCount(solvedCount)
                .correctCount(correctCount)
                .accuracy(accuracy)
                .completedAt(completedAt)
                .build();
    }

    private void validateSubmitInput(Long userId, Long attemptId, QuizSubmitRequest request) {
        if (userId != null && userId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (attemptId == null || attemptId <= 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        if (request == null || request.getSeq() == null || request.getSeq() < 1) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private EvaluationResult evaluateAnswer(Long questionId, String questionType, QuizSubmitRequest request) {
        boolean sentenceMode = "SENTENCE".equalsIgnoreCase(questionType)
                || (request.getOrderedTokenIds() != null && !request.getOrderedTokenIds().isEmpty());

        if (sentenceMode) {
            List<Long> tokenIds = request.getOrderedTokenIds();
            if (tokenIds == null || tokenIds.isEmpty()) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
            if (tokenIds.stream().anyMatch(id -> id == null || id <= 0)) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }

            String submitted = toCsv(tokenIds);
            String expected = quizCommandMapper.findSentenceTokenOrderCsv(questionId);
            if (expected == null || expected.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
            return new EvaluationResult(expected.equals(submitted), submitted);
        }

        if (request.getChoiceId() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        Integer correctFlag = quizCommandMapper.findChoiceCorrectFlag(questionId, request.getChoiceId());
        if (correctFlag == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return new EvaluationResult(correctFlag == 1, null);
    }

    private String toCsv(List<Long> tokenIds) {
        return tokenIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private void validateCompleteInput(Long userId, Long attemptId) {
        if (userId != null && userId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (attemptId == null || attemptId <= 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateResultInput(Long userId, Long attemptId) {
        if (userId == null || userId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (attemptId == null || attemptId <= 0) {
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

    private String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    private LocalDateTime castToLocalDateTime(Object value) {
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    private void updateWrongAnswerNote(Long userId, Long questionId, boolean correct) {
        if (correct) {
            wrongAnswerCommandService.deleteWrongAnswer(userId, questionId);
            return;
        }

        wrongAnswerCommandService.saveWrongAnswer(userId, new WrongAnswerSaveRequest(questionId));
    }

    private boolean isAllowedOwner(Long ownerId, Long currentUserId) {
        if (ownerId == null) {
            return currentUserId == null;
        }
        return ownerId.equals(currentUserId);
    }

    private record EvaluationResult(boolean correct, String submittedTokenOrder) {
    }
}
