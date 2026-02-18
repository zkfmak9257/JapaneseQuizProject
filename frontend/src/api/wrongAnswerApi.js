import { http, unwrap } from "./http";

export async function getWrongAnswers(page = 1, size = 10) {
  const response = await http.get("/api/quiz/wrong-answers", {
    params: { page, size }
  });
  return unwrap(response);
}
