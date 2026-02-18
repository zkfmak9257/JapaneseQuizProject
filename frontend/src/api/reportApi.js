import { http, unwrap } from "./http";

export async function createReport(questionId, reportType, content) {
  const response = await http.post("/api/reports", {
    questionId,
    reportType,
    content
  });
  return unwrap(response);
}
