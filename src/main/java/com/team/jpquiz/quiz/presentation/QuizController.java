package com.team.jpquiz.quiz.presentation;


import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.ErrorResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Quiz", description = "퀴즈 API")
public class QuizController {
    private final QuizQueryService quizQueryService;
    private final QuizCommandService quizCommandService;

    @Operation(
            summary = "퀴즈 시작",
            description = """
                    새로운 퀴즈 시도(Attempt)를 생성하고 출제 문항을 고정합니다.
                    비회원/회원 모두 호출 가능하며, 응답으로 attemptId를 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "퀴즈 시작 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 값 오류(예: 잘못된 questionType/sceneId)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "문제 세트 생성 중 서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/attempts/start")
    public ApiResponse<QuizAttemptResponse> startQuiz(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "퀴즈 시작 옵션(문제 타입/카테고리). 생략 시 기본 랜덤 출제",
                    required = false
            )
            @RequestBody(required = false) StartQuizRequest request
    ) {
        Long userId = SecurityUtil.getCurrentMemberIdOrNull();
        QuizAttemptResponse response = quizCommandService.startQuiz(userId, request);
        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "퀴즈 문항 조회",
            description = "attemptId와 seq에 해당하는 문항(문제/보기/문장 토큰)을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "문항 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 값 오류(예: seq < 1)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 Attempt의 문항을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/attempts/{attemptId}/questions/{seq}")
    public ApiResponse<QuizAttemptQuestionResponse> getAttemptQuestion(
            @Parameter(description = "퀴즈 시도 ID", example = "1001", required = true)
            @PathVariable Long attemptId,
            @Parameter(description = "문항 순번", example = "1", required = true)
            @PathVariable int seq
    ) {
        QuizAttemptQuestionResponse response = quizQueryService.findAttemptQuestion(attemptId, seq);
        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "답안 제출 및 채점",
            description = """
                    attemptId와 seq에 해당하는 문항에 대한 사용자의 답안을 제출합니다.
                    서버는 제출 즉시 채점하고, 피드백(정답/해설/보기/문장 토큰 등)을 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채점 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 값 오류(예: seq/choiceId 형식 오류, 이미 제출한 문항)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "해당 Attempt 접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Attempt 또는 문항을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/attempts/{attemptId}/answers")
    public ApiResponse<QuizAnswerResultResponse> submitAnswer(
            @Parameter(
                    description = "퀴즈 시도 ID(start API 호출로 발급된 attemptId)",
                    example = "1001",
                    required = true
            )
            @PathVariable Long attemptId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "답안 제출 요청 본문",
                    required = true
            )
            @Valid @RequestBody QuizSubmitRequest request
    ) {
        Long userId = SecurityUtil.getCurrentMemberIdOrNull();
        QuizAnswerResultResponse response = quizCommandService.submitAnswer(userId, attemptId, request);
        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "퀴즈 완료 처리",
            description = "해당 attemptId에 대한 퀴즈를 완료 상태로 전이시키고 결과 집계를 수행합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "퀴즈 완료 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 완료된 Attempt"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Attempt를 찾을 수 없음")
    })
    @PostMapping("/attempts/{attemptId}/complete")
    public ApiResponse<QuizCompleteResponse> complete(
            @Parameter(description = "퀴즈 시도 ID", example = "1")
            @PathVariable Long attemptId
    ) {
        Long userId = SecurityUtil.getCurrentMemberIdOrNull();
        QuizCompleteResponse response = quizCommandService.completeQuiz(userId, attemptId);
        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "퀴즈 결과 조회",
            description = """
                    완료된 attemptId의 결과를 조회합니다.
                    본인 시도만 조회 가능하며, 완료되지 않은 시도는 결과 조회가 불가합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결과 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "아직 완료되지 않은 Attempt",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 필요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "타인의 Attempt 접근 불가",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Attempt를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/attempts/{attemptId}/result")
    public ApiResponse<QuizResultResponse> getQuizResult(
            @Parameter(description = "퀴즈 시도 ID", example = "1001", required = true)
            @PathVariable Long attemptId
    ) {
        Long userId = SecurityUtil.getCurrentMemberId();
        QuizResultResponse response = quizCommandService.getQuizResult(userId, attemptId);
        return ApiResponse.ok(response);
    }

}
