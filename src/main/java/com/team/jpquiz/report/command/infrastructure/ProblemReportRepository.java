package com.team.jpquiz.report.command.infrastructure;

import com.team.jpquiz.report.command.domain.ProblemReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemReportRepository extends JpaRepository<ProblemReport, Long> {
}
