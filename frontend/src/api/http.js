import axios from "axios";

const configuredBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim();

const http = axios.create({
  // Ingress 단일 진입점 기준 기본값은 /boot 를 사용합니다.
  baseURL: configuredBaseUrl || "/boot",
  timeout: 10000
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let isRefreshing = false;
let failedQueue = [];

function processQueue(error, token) {
  failedQueue.forEach(({ resolve, reject }) => {
    if (error) {
      reject(error);
    } else {
      resolve(token);
    }
  });
  failedQueue = [];
}

http.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 401이 아니거나, 이미 재시도한 요청이면 그대로 reject
    if (error?.response?.status !== 401 || originalRequest._retry) {
      return Promise.reject(error);
    }

    // 인증 관련 API는 리프레시 시도하지 않음
    const url = originalRequest.url || "";
    if (url.includes("/api/auth/login") || url.includes("/api/auth/register") || url.includes("/api/auth/refresh")) {
      return Promise.reject(error);
    }

    const refreshToken = localStorage.getItem("refreshToken");
    if (!refreshToken) {
      clearAuthAndRedirect();
      return Promise.reject(error);
    }

    // 이미 리프레시 중이면 큐에 넣고 대기
    if (isRefreshing) {
      return new Promise((resolve, reject) => {
        failedQueue.push({ resolve, reject });
      }).then((token) => {
        originalRequest._retry = true; // 재시도 중복 방지
        originalRequest.headers.Authorization = `Bearer ${token}`;
        return http(originalRequest);
      });
    }

    originalRequest._retry = true;
    isRefreshing = true;

    try {
      const { data } = await http.post("/api/auth/refresh", { refreshToken });

      const newAccessToken = data?.data?.accessToken;
      const newRefreshToken = data?.data?.refreshToken;

      // 토큰이 정상적으로 발급되지 않으면 로그아웃 처리
      if (!newAccessToken) {
        throw new Error("액세스 토큰 발급 실패");
      }

      localStorage.setItem("accessToken", newAccessToken);
      if (newRefreshToken) {
        localStorage.setItem("refreshToken", newRefreshToken);
      }

      processQueue(null, newAccessToken);
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
      return http(originalRequest);
    } catch (refreshError) {
      processQueue(refreshError, null);
      clearAuthAndRedirect();
      return Promise.reject(refreshError);
    } finally {
      isRefreshing = false;
    }
  }
);

function clearAuthAndRedirect() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  const currentPath = window.location.pathname;
  const isQuizAttemptFlow = currentPath.startsWith("/quiz/attempts/");
  if (!isQuizAttemptFlow && currentPath !== "/login") {
    window.location.href = "/login";
  }
}

function unwrap(response) {
  return response?.data?.data;
}

export { http, unwrap };
