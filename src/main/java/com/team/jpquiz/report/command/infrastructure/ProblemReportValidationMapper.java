package com.team.jpquiz.report.command.infrastructure;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProblemReportValidationMapper {

    int countActiveQuestionById(@Param("questionId") Long questionId);

    int countDistinctReporterIdsByQuestionId(@Param("questionId") Long questionId);

    int deactivateQuestionById(@Param("questionId") Long questionId);
}
