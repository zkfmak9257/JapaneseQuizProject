import { http, unwrap } from "./http";

export async function login(payload) {
  const response = await http.post("/api/auth/login", payload);
  return unwrap(response);
}

export async function register(payload) {
  const response = await http.post("/api/auth/register", payload);
  return unwrap(response);
}
