import { http, unwrap } from "./http";

export async function startQuiz() {
  const response = await http.post("/api/quiz/attempts/start");
  return unwrap(response);
}

export async function getAttemptQuestion(attemptId, seq) {
  const response = await http.get(`/api/quiz/attempts/${attemptId}/questions/${seq}`);
  return unwrap(response);
}

export async function submitAnswer(attemptId, seq, choiceId) {
  const response = await http.post(`/api/quiz/attempts/${attemptId}/answers`, { seq, choiceId });
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
