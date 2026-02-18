import { http, unwrap } from "./http";

export async function getMyStats() {
  const response = await http.get("/api/stats/me");
  return unwrap(response);
}
