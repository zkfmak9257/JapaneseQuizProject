import { http, unwrap } from "./http";

export async function login(payload) {
  const response = await http.post("/api/auth/login", payload);
  return unwrap(response);
}
