package com.team.jpquiz.report.dto.request;

import com.team.jpquiz.report.command.domain.ReportStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemReportStatusUpdateRequest {

    @NotNull
    private ReportStatus status;

    @NotBlank
    @Size(max = 500)
    private String action;
}
