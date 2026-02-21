import { http, unwrap } from "./http";

export async function getMyStats() {
  const response = await http.get("/api/stats/me");
  return unwrap(response);
}

export async function getAdminOverview() {
  const response = await http.get("/api/admin/stats/overview");
  return unwrap(response);
}

export async function getCategoryAccuracy(basis = "latest") {
  const response = await http.get("/api/admin/stats/categories", {
    params: { basis }
  });
  return unwrap(response);
}

export async function getQuestionStats({ basis = "first", page = 1, size = 10 } = {}) {
  const response = await http.get("/api/admin/stats/questions", {
    params: { basis, page, size }
  });
  return unwrap(response);
}

export async function getTopWrongQuestions(limit = 10) {
  const response = await http.get("/api/admin/stats/questions/top-wrong", {
    params: { limit }
  });
  return unwrap(response);
}
