import { http, unwrap } from "./http";

export async function getFavorites(page = 1, size = 10, category = null) {
  const response = await http.get("/api/favorites", {
    params: { page, size, category: category || undefined }
  });
  return unwrap(response);
}

export async function toggleFavorite(questionId) {
  const response = await http.post(`/api/favorites/${questionId}/toggle`);
  return unwrap(response);
}

export async function createFavoriteReviewSet(category) {
  const response = await http.post("/api/favorites/review-set", null, {
    params: { category: category || undefined }
  });
  return unwrap(response);
}

export async function createSingleFavoriteAttempt(questionId) {
  const response = await http.post(`/api/favorites/${questionId}/single-attempt`);
  return unwrap(response);
}
