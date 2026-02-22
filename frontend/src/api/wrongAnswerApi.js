import { http, unwrap } from "./http";

export async function getWrongAnswers(page = 1, size = 10, sort = 'recent', category = 'ALL') {
  const params = { page, size };
  if (sort) params.sort = sort;
  if (category && category !== 'ALL') params.category = category;

  const response = await http.get("/api/quiz/wrong-answers", { params });
  return unwrap(response);
}

export async function removeWrongAnswer(questionId) {
  // 실제 백엔드 지원 여부에 맞춰 조정 필요 (현재 DELETE 가정)
  const response = await http.delete(`/api/quiz/wrong-answers/${questionId}`);
  return unwrap(response);
}
