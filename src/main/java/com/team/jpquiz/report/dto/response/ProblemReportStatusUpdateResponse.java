package com.team.jpquiz.report.dto.response;

import com.team.jpquiz.report.command.domain.ProblemReport;
import com.team.jpquiz.report.command.domain.ReportStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemReportStatusUpdateResponse {
    private Long reportId;
    private ReportStatus status;
    private String action;
    private LocalDateTime updatedAt;

    public static ProblemReportStatusUpdateResponse from(ProblemReport problemReport) {
        return ProblemReportStatusUpdateResponse.builder()
                .reportId(problemReport.getReportId())
                .status(problemReport.getStatus())
                .action(problemReport.getAction())
                .updatedAt(problemReport.getUpdatedAt())
                .build();
    }
}
