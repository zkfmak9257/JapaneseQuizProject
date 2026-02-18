import { http, unwrap } from "./http";

export async function getMyProfile() {
  const response = await http.get("/api/members/me");
  return unwrap(response);
}

export async function updateMyProfile(payload) {
  const response = await http.patch("/api/members/me", payload);
  return unwrap(response);
}
