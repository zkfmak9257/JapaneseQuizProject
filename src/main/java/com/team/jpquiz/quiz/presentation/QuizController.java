package com.team.jpquiz.quiz.presentation;


import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.quiz.command.application.QuizCommandService;
import com.team.jpquiz.quiz.dto.request.StartQuizRequest;
import com.team.jpquiz.quiz.dto.response.QuizAttemptQuestionResponse;
import com.team.jpquiz.quiz.dto.response.QuizAttemptResponse;
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
            @Valid @RequestBody StartQuizRequest request
    ) {
        Long userId = SecurityUtil.getCurrentMemberId();
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

}
