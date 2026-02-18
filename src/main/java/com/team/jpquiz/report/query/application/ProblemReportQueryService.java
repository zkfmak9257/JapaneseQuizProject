package com.team.jpquiz.report.query.application;

import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.report.command.domain.ReportStatus;
import com.team.jpquiz.report.dto.response.ProblemReportAdminResponse;
import com.team.jpquiz.report.query.infrastructure.ProblemReportMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemReportQueryService {

    private final ProblemReportMapper problemReportMapper;

    public PageResponse<ProblemReportAdminResponse> getReportList(
            int page,
            int size,
            String status
    ) {
        validatePaging(page, size);

        int offset = (page - 1) * size;
        String statusName = normalizeStatus(status);

        List<ProblemReportAdminResponse> content =
                problemReportMapper.findReports(statusName, offset, size);
        long totalElements = problemReportMapper.countReports(statusName);

        return PageResponse.of(content, page, size, totalElements);
    }

    private void validatePaging(int page, int size) {
        if (page < 1 || size < 1 || size > 100) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return ReportStatus.valueOf(status.trim().toUpperCase()).name();
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}
