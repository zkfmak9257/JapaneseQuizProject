package com.team.jpquiz.report.command.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.report.command.domain.ProblemReport;
import com.team.jpquiz.report.command.domain.ReportStatus;
import com.team.jpquiz.report.command.infrastructure.ProblemReportRepository;
import com.team.jpquiz.report.command.infrastructure.ProblemReportValidationMapper;
import com.team.jpquiz.report.dto.request.ProblemReportCreateRequest;
import com.team.jpquiz.report.dto.request.ProblemReportStatusUpdateRequest;
import com.team.jpquiz.report.dto.response.ProblemReportCreateResponse;
import com.team.jpquiz.report.dto.response.ProblemReportStatusUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemReportCommandService {

    private final ProblemReportRepository problemReportRepository;
    private final ProblemReportValidationMapper problemReportValidationMapper;

    public ProblemReportCreateResponse createReport(Long reporterId, ProblemReportCreateRequest request) {
        validateReporterId(reporterId);
        validateQuestionId(request.getQuestionId());

        ProblemReport problemReport = ProblemReport.builder()
                .questionId(request.getQuestionId())
                .reporterId(reporterId)
                .reportType(request.getReportType())
                .content(request.getContent())
                .build();

        ProblemReport saved = problemReportRepository.save(problemReport);
        return ProblemReportCreateResponse.from(saved);
    }

    public ProblemReportStatusUpdateResponse updateReportStatus(
            Long reportId,
            ProblemReportStatusUpdateRequest request
    ) {
        ProblemReport problemReport = problemReportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        ReportStatus currentStatus = problemReport.getStatus();
        if (!currentStatus.canTransitionTo(request.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_REPORT_STATUS_TRANSITION);
        }

        problemReport.updateStatus(request.getStatus(), request.getAction());
        return ProblemReportStatusUpdateResponse.from(problemReport);
    }

    private void validateReporterId(Long reporterId) {
        if (reporterId != null && reporterId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void validateQuestionId(Long questionId) {
        if (problemReportValidationMapper.countQuestionById(questionId) == 0) {
            throw new CustomException(ErrorCode.REPORT_TARGET_QUESTION_NOT_FOUND);
        }
    }
}
