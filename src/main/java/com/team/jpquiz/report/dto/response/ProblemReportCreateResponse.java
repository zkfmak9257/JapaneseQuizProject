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
public class ProblemReportCreateResponse {
    private Long reportId;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public static ProblemReportCreateResponse from(ProblemReport problemReport) {
        return ProblemReportCreateResponse.builder()
                .reportId(problemReport.getReportId())
                .status(problemReport.getStatus())
                .createdAt(problemReport.getCreatedAt())
                .build();
    }
}
