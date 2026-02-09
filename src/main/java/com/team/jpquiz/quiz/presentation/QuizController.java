package com.team.jpquiz.quiz.presentation;


import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.quiz.dto.response.QuizAttemptQuestionResponse;
import com.team.jpquiz.quiz.query.application.QuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizQueryService quizQueryService;

    @GetMapping("/attempts/{attemptId}/questions/{seq}")
    public ApiResponse<QuizAttemptQuestionResponse> getAttemptQuestion(
            @PathVariable Long attemptId,
            @PathVariable int seq
    ) {
        QuizAttemptQuestionResponse response = quizQueryService.findAttemptQuestion(attemptId, seq);
        return ApiResponse.ok(response);
    }

}
