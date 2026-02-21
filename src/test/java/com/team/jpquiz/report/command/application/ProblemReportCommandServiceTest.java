package com.team.jpquiz.report.command.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.report.command.domain.ProblemReport;
import com.team.jpquiz.report.command.domain.ReportStatus;
import com.team.jpquiz.report.command.domain.ReportType;
import com.team.jpquiz.report.command.infrastructure.ProblemReportRepository;
import com.team.jpquiz.report.command.infrastructure.ProblemReportValidationMapper;
import com.team.jpquiz.report.dto.request.ProblemReportCreateRequest;
import com.team.jpquiz.report.dto.request.ProblemReportStatusUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProblemReportCommandServiceTest {

    @Mock
    private ProblemReportRepository problemReportRepository;

    @Mock
    private ProblemReportValidationMapper problemReportValidationMapper;

    @InjectMocks
    private ProblemReportCommandService problemReportCommandService;

    @Test
    void createReport_typoWithBlankContent_usesDefaultContent() {
        ProblemReportCreateRequest request = createRequest(1L, ReportType.TYPO, "   ");
        given(problemReportValidationMapper.countActiveQuestionById(1L)).willReturn(1);
        given(problemReportRepository.save(any(ProblemReport.class))).willAnswer(invocation -> invocation.getArgument(0));

        problemReportCommandService.createReport(10L, request);

        ArgumentCaptor<ProblemReport> captor = ArgumentCaptor.forClass(ProblemReport.class);
        verify(problemReportRepository).save(captor.capture());
        assertThat(captor.getValue().getContent()).isEqualTo("오탈자가 있습니다.");
    }

    @Test
    void createReport_wrongAnswerWithNullContent_usesDefaultContent() {
        ProblemReportCreateRequest request = createRequest(1L, ReportType.WRONG_ANSWER, null);
        given(problemReportValidationMapper.countActiveQuestionById(1L)).willReturn(1);
        given(problemReportRepository.save(any(ProblemReport.class))).willAnswer(invocation -> invocation.getArgument(0));

        problemReportCommandService.createReport(10L, request);

        ArgumentCaptor<ProblemReport> captor = ArgumentCaptor.forClass(ProblemReport.class);
        verify(problemReportRepository).save(captor.capture());
        assertThat(captor.getValue().getContent()).isEqualTo("정답에 오류가 있습니다.");
    }

    @Test
    void createReport_etcWithBlankContent_throwsValidationException() {
        ProblemReportCreateRequest request = createRequest(1L, ReportType.ETC, " ");
        given(problemReportValidationMapper.countActiveQuestionById(1L)).willReturn(1);

        assertThatThrownBy(() -> problemReportCommandService.createReport(10L, request))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> assertThat(((CustomException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.REPORT_CONTENT_REQUIRED_FOR_ETC));
    }

    @Test
    void createReport_typoWithContent_preservesTrimmedContent() {
        ProblemReportCreateRequest request = createRequest(1L, ReportType.TYPO, "  실제 상세 사유 ");
        given(problemReportValidationMapper.countActiveQuestionById(1L)).willReturn(1);
        given(problemReportRepository.save(any(ProblemReport.class))).willAnswer(invocation -> invocation.getArgument(0));

        problemReportCommandService.createReport(10L, request);

        ArgumentCaptor<ProblemReport> captor = ArgumentCaptor.forClass(ProblemReport.class);
        verify(problemReportRepository).save(captor.capture());
        assertThat(captor.getValue().getContent()).isEqualTo("실제 상세 사유");
    }

    @Test
    void updateReportStatus_resolved_deactivatesQuestion() {
        ProblemReport problemReport = ProblemReport.builder()
                .questionId(123L)
                .reporterId(10L)
                .reportType(ReportType.TYPO)
                .content("오탈자")
                .build();
        ReflectionTestUtils.setField(problemReport, "reportId", 1L);
        ReflectionTestUtils.setField(problemReport, "status", ReportStatus.PENDING);

        ProblemReportStatusUpdateRequest request = createStatusUpdateRequest(ReportStatus.RESOLVED, "검수 완료");
        given(problemReportRepository.findById(1L)).willReturn(java.util.Optional.of(problemReport));
        ReflectionTestUtils.setField(problemReportCommandService, "autoDeactivateOnResolved", true);

        problemReportCommandService.updateReportStatus(1L, request);

        verify(problemReportValidationMapper).deactivateQuestionById(123L);
    }

    @Test
    void updateReportStatus_rejected_doesNotDeactivateQuestion() {
        ProblemReport problemReport = ProblemReport.builder()
                .questionId(123L)
                .reporterId(10L)
                .reportType(ReportType.TYPO)
                .content("오탈자")
                .build();
        ReflectionTestUtils.setField(problemReport, "reportId", 1L);
        ReflectionTestUtils.setField(problemReport, "status", ReportStatus.PENDING);

        ProblemReportStatusUpdateRequest request = createStatusUpdateRequest(ReportStatus.REJECTED, "근거 부족");
        given(problemReportRepository.findById(1L)).willReturn(java.util.Optional.of(problemReport));
        ReflectionTestUtils.setField(problemReportCommandService, "autoDeactivateOnResolved", true);

        problemReportCommandService.updateReportStatus(1L, request);

        verify(problemReportValidationMapper, never()).deactivateQuestionById(any(Long.class));
    }

    private ProblemReportCreateRequest createRequest(Long questionId, ReportType reportType, String content) {
        ProblemReportCreateRequest request = new ProblemReportCreateRequest();
        ReflectionTestUtils.setField(request, "questionId", questionId);
        ReflectionTestUtils.setField(request, "reportType", reportType);
        ReflectionTestUtils.setField(request, "content", content);
        return request;
    }

    private ProblemReportStatusUpdateRequest createStatusUpdateRequest(ReportStatus status, String action) {
        ProblemReportStatusUpdateRequest request = new ProblemReportStatusUpdateRequest();
        ReflectionTestUtils.setField(request, "status", status);
        ReflectionTestUtils.setField(request, "action", action);
        return request;
    }
}
