package com.team.jpquiz.report.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.report.command.application.ProblemReportCommandService;
import com.team.jpquiz.report.dto.request.ProblemReportStatusUpdateRequest;
import com.team.jpquiz.report.dto.response.ProblemReportAdminResponse;
import com.team.jpquiz.report.dto.response.ProblemReportStatusUpdateResponse;
import com.team.jpquiz.report.query.application.ProblemReportQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProblemReportController {

    private final ProblemReportQueryService problemReportQueryService;
    private final ProblemReportCommandService problemReportCommandService;

    @GetMapping
    public ApiResponse<PageResponse<ProblemReportAdminResponse>> getReportList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {
        PageResponse<ProblemReportAdminResponse> response =
                problemReportQueryService.getReportList(page, size, status);
        return ApiResponse.ok(response);
    }

    @PatchMapping("/{reportId}")
    public ApiResponse<ProblemReportStatusUpdateResponse> updateReportStatus(
            @PathVariable Long reportId,
            @Valid @RequestBody ProblemReportStatusUpdateRequest request
    ) {
        ProblemReportStatusUpdateResponse response =
                problemReportCommandService.updateReportStatus(reportId, request);
        return ApiResponse.ok(response);
    }
}
