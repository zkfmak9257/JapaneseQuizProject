package com.team.jpquiz.report.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.global.security.UserPrincipal;
import com.team.jpquiz.report.command.application.ProblemReportCommandService;
import com.team.jpquiz.report.dto.request.ProblemReportCreateRequest;
import com.team.jpquiz.report.dto.response.ProblemReportCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ProblemReportController {

    private final ProblemReportCommandService problemReportCommandService;

    @PostMapping
    public ApiResponse<ProblemReportCreateResponse> createReport(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProblemReportCreateRequest request
    ) {
        Long reporterId = userPrincipal != null ? userPrincipal.getUserId() : null;
        ProblemReportCreateResponse response = problemReportCommandService.createReport(reporterId, request);
        return ApiResponse.ok(response);
    }
}
