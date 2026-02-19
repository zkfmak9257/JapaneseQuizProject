import { http, unwrap } from "./http";

export async function getFavorites(page = 1, size = 10, category = null) {
  const response = await http.get("/api/favorites", {
    params: { page, size, category: category || undefined }
  });
  return unwrap(response);
}
