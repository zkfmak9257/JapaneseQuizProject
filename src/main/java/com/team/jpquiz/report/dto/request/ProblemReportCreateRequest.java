package com.team.jpquiz.report.dto.request;

import com.team.jpquiz.report.command.domain.ReportType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemReportCreateRequest {

    @NotNull
    @Positive
    private Long questionId;

    @NotNull
    private ReportType reportType;

    @Size(max = 1000)
    private String content;
}
