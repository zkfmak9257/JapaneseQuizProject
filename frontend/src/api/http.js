import axios from "axios";

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
  timeout: 10000
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error?.response?.status === 401) {
      localStorage.removeItem("accessToken");
      const currentPath = window.location.pathname;
      const isQuizAttemptFlow = currentPath.startsWith("/quiz/attempts/");
      if (!isQuizAttemptFlow && currentPath !== "/login") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

function unwrap(response) {
  return response?.data?.data;
}

export { http, unwrap };
