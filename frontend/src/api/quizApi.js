import { http, unwrap } from "./http";

export async function startQuiz(payload = {}) {
  const response = await http.post("/api/quiz/attempts/start", payload);
  return unwrap(response);
}

export async function getAttemptQuestion(attemptId, seq) {
  const response = await http.get(`/api/quiz/attempts/${attemptId}/questions/${seq}`);
  return unwrap(response);
}

export async function submitAnswer(attemptId, payload) {
  const response = await http.post(`/api/quiz/attempts/${attemptId}/answers`, payload);
  return unwrap(response);
}

export async function completeAttempt(attemptId) {
  const response = await http.post(`/api/quiz/attempts/${attemptId}/complete`);
  return unwrap(response);
}

export async function getResult(attemptId) {
  const response = await http.get(`/api/quiz/attempts/${attemptId}/result`);
  return unwrap(response);
}
