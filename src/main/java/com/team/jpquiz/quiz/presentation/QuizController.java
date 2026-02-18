package com.team.jpquiz.quiz.presentation;


import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.quiz.command.application.QuizCommandService;
import com.team.jpquiz.quiz.dto.request.QuizSubmitRequest;
import com.team.jpquiz.quiz.dto.request.StartQuizRequest;
import com.team.jpquiz.quiz.dto.response.QuizAnswerResultResponse;
import com.team.jpquiz.quiz.dto.response.QuizAttemptQuestionResponse;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
import com.team.jpquiz.quiz.dto.response.QuizCompleteResponse;
import com.team.jpquiz.quiz.dto.response.QuizResultResponse;
import com.team.jpquiz.quiz.query.application.QuizQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizQueryService quizQueryService;
    private final QuizCommandService quizCommandService;

    @PostMapping("/attempts/start")
    public ApiResponse<QuizAttemptResponse> startQuiz(
            @RequestBody(required = false) StartQuizRequest request
    ) {
        Long userId = SecurityUtil.getCurrentMemberIdOrNull();
        QuizAttemptResponse response = quizCommandService.startQuiz(userId, request);
        return ApiResponse.ok(response);
    }

    @GetMapping("/attempts/{attemptId}/questions/{seq}")
    public ApiResponse<QuizAttemptQuestionResponse> getAttemptQuestion(
            @PathVariable Long attemptId,
            @PathVariable int seq
    ) {
        QuizAttemptQuestionResponse response = quizQueryService.findAttemptQuestion(attemptId, seq);
        return ApiResponse.ok(response);
    }

    @PostMapping("/attempts/{attemptId}/answers")
    public ApiResponse<QuizAnswerResultResponse> submitAnswer(
            @PathVariable Long attemptId,
            @Valid @RequestBody QuizSubmitRequest request
    ) {
        Long userId = SecurityUtil.getCurrentMemberIdOrNull();
        QuizAnswerResultResponse response = quizCommandService.submitAnswer(userId, attemptId, request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/attempts/{attemptId}/complete")
    public ApiResponse<QuizCompleteResponse> completeQuiz(
            @PathVariable Long attemptId
    ) {
        Long userId = SecurityUtil.getCurrentMemberIdOrNull();
        QuizCompleteResponse response = quizCommandService.completeQuiz(userId, attemptId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/attempts/{attemptId}/result")
    public ApiResponse<QuizResultResponse> getQuizResult(
            @PathVariable Long attemptId
    ) {
        Long userId = SecurityUtil.getCurrentMemberId();
        QuizResultResponse response = quizCommandService.getQuizResult(userId, attemptId);
        return ApiResponse.ok(response);
    }

}
