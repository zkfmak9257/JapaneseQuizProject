package com.team.jpquiz.report.command.infrastructure;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProblemReportValidationMapper {

    int countQuestionById(@Param("questionId") Long questionId);
}
