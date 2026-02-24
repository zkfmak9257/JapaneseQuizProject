import { http, unwrap } from "./http";

export async function createReport(questionId, reportType, content) {
  const response = await http.post("/api/reports", {
    questionId,
    reportType,
    content
  });
  return unwrap(response);
}

// 관리자: 신고 목록 조회
export async function getAdminReports(page = 1, size = 10, status = null) {
  const params = { page, size };
  if (status) params.status = status;
  const response = await http.get("/api/admin/reports", { params });
  return unwrap(response);
}

// 관리자: 신고 상태 변경
export async function updateReportStatus(reportId, status, action) {
  const response = await http.patch(`/api/admin/reports/${reportId}`, { status, action });
  return unwrap(response);
}
