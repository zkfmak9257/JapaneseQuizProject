package com.team.jpquiz.report.query.infrastructure;

import com.team.jpquiz.report.dto.response.ProblemReportAdminResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProblemReportMapper {

    List<ProblemReportAdminResponse> findReports(
            @Param("status") String status,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countReports(@Param("status") String status);
}
