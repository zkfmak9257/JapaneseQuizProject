package com.team.jpquiz.report.command.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.report.command.domain.ProblemReport;
import com.team.jpquiz.report.command.domain.ReportStatus;
import com.team.jpquiz.report.command.domain.ReportType;
import com.team.jpquiz.report.command.infrastructure.ProblemReportRepository;
import com.team.jpquiz.report.command.infrastructure.ProblemReportValidationMapper;
import com.team.jpquiz.report.dto.request.ProblemReportCreateRequest;
import com.team.jpquiz.report.dto.request.ProblemReportStatusUpdateRequest;
import com.team.jpquiz.report.dto.response.ProblemReportCreateResponse;
import com.team.jpquiz.report.dto.response.ProblemReportStatusUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemReportCommandService {

    private static final String DEFAULT_TYPO_CONTENT = "오탈자가 있습니다.";
    private static final String DEFAULT_WRONG_ANSWER_CONTENT = "정답에 오류가 있습니다.";

    private final ProblemReportRepository problemReportRepository;
    private final ProblemReportValidationMapper problemReportValidationMapper;

    @Value("${app.report.auto-deactivate.unique-reporter-threshold:3}")
    private int autoDeactivateUniqueReporterThreshold;

    public ProblemReportCreateResponse createReport(Long reporterId, ProblemReportCreateRequest request) {
        validateReporterId(reporterId);
        validateQuestionId(request.getQuestionId());
        ReportType reportType = request.getReportType();
        if (reportType == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        String content = resolveContent(reportType, request.getContent());

        ProblemReport problemReport = ProblemReport.builder()
                .questionId(request.getQuestionId())
                .reporterId(reporterId)
                .reportType(reportType)
                .content(content)
                .build();

        ProblemReport saved = problemReportRepository.save(problemReport);
        applyAutoDeactivationPolicy(saved.getQuestionId(), reporterId);
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
        if (problemReportValidationMapper.countActiveQuestionById(questionId) == 0) {
            throw new CustomException(ErrorCode.REPORT_TARGET_QUESTION_NOT_FOUND);
        }
    }

    private String resolveContent(ReportType reportType, String content) {
        String trimmed = content == null ? null : content.trim();
        if (trimmed != null && !trimmed.isEmpty()) {
            return trimmed;
        }

        return switch (reportType) {
            case TYPO -> DEFAULT_TYPO_CONTENT;
            case WRONG_ANSWER -> DEFAULT_WRONG_ANSWER_CONTENT;
            case ETC -> throw new CustomException(ErrorCode.REPORT_CONTENT_REQUIRED_FOR_ETC);
        };
    }

    private void applyAutoDeactivationPolicy(Long questionId, Long reporterId) {
        if (reporterId == null || autoDeactivateUniqueReporterThreshold <= 0) {
            return;
        }

        int distinctReporterCount = problemReportValidationMapper.countDistinctReporterIdsByQuestionId(questionId);
        if (distinctReporterCount < autoDeactivateUniqueReporterThreshold) {
            return;
        }

        problemReportValidationMapper.deactivateQuestionById(questionId);
    }
}
