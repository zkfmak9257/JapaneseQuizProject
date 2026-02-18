package com.team.jpquiz.report.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemReportAdminResponse {
    private Long reportId;
    private Long questionId;
    private Long reporterId;
    private String reportType;
    private String content;
    private String status;
    private String action;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
