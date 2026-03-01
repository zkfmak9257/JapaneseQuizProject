import { http, unwrap } from "./http";

export async function getWrongAnswers(page = 1, size = 10, sort = 'recent', category = 'ALL') {
  const params = { page, size };
  if (sort) params.sort = sort;
  if (category && category !== 'ALL') params.category = category;

  const response = await http.get("/api/quiz/wrong-answers", { params });
  return unwrap(response);
}

export async function removeWrongAnswer(questionId) {
  const response = await http.delete(`/api/quiz/wrong-answers/${questionId}`);
  return unwrap(response);
}

// 오답 복습 세트 생성 (카테고리 필터 시 해당 오답만, 전체는 10문제 고정)
export async function createReviewSet(category = 'ALL') {
  const response = await http.post("/api/quiz/wrong-answers/review-set", null, {
    params: { category }
  });
  return unwrap(response);
}

// 단일 문제 재도전 attempt 생성
export async function createSingleQuestionAttempt(questionId) {
  const response = await http.post(`/api/quiz/wrong-answers/${questionId}/single-attempt`);
  return unwrap(response);
}
