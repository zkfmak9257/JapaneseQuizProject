# Refresh Token 갱신 로직 추가

## 문제
- Access Token(30분) 만료 후 401 에러 발생 시, Refresh Token으로 갱신하지 않고 바로 로그인 페이지로 리다이렉트됨
- Refresh Token을 localStorage에 저장하지 않아 갱신 자체가 불가능

---

## 수정 파일 목록

### 1. `frontend/src/stores/authStore.js`

#### 수정 전
```js
const accessToken = ref(localStorage.getItem("accessToken") || "");
const profile = ref(
  localStorage.getItem("authProfile") ? JSON.parse(localStorage.getItem("authProfile")) : null
);

function setToken(token) {
  accessToken.value = token || "";
  if (accessToken.value) {
    localStorage.setItem("accessToken", accessToken.value);
  } else {
    localStorage.removeItem("accessToken");
  }
}

async function login(email, password) {
  const token = await loginApi({ email, password });
  setToken(token.accessToken);
  await fetchMe();
}

async function register(email, nickname, password) {
  const token = await registerApi({ email, nickname, password });
  setToken(token.accessToken);
  await fetchMe();
}

function logout() {
  setToken("");
  setProfile(null);
}

return {
  accessToken,
  profile,
  role,
  isAdmin,
  isLoggedIn,
  login,
  register,
  fetchMe,
  logout,
  setToken,
  setProfile
};
```

#### 수정 후
```js
const accessToken = ref(localStorage.getItem("accessToken") || "");
const refreshToken = ref(localStorage.getItem("refreshToken") || "");
const profile = ref(
  localStorage.getItem("authProfile") ? JSON.parse(localStorage.getItem("authProfile")) : null
);

function setToken(token) {
  accessToken.value = token || "";
  if (accessToken.value) {
    localStorage.setItem("accessToken", accessToken.value);
  } else {
    localStorage.removeItem("accessToken");
  }
}

// [추가] refreshToken 저장/삭제 함수
function setRefreshToken(token) {
  refreshToken.value = token || "";
  if (refreshToken.value) {
    localStorage.setItem("refreshToken", refreshToken.value);
  } else {
    localStorage.removeItem("refreshToken");
  }
}

async function login(email, password) {
  const token = await loginApi({ email, password });
  setToken(token.accessToken);
  setRefreshToken(token.refreshToken);  // [추가]
  await fetchMe();
}

async function register(email, nickname, password) {
  const token = await registerApi({ email, nickname, password });
  setToken(token.accessToken);
  setRefreshToken(token.refreshToken);  // [추가]
  await fetchMe();
}

function logout() {
  setToken("");
  setRefreshToken("");  // [추가]
  setProfile(null);
}

return {
  accessToken,
  refreshToken,      // [추가]
  profile,
  role,
  isAdmin,
  isLoggedIn,
  login,
  register,
  fetchMe,
  logout,
  setToken,
  setRefreshToken,   // [추가]
  setProfile
};
```

**변경 요약:**
- `refreshToken` state 추가
- `setRefreshToken()` 함수 추가 (localStorage 연동)
- `login()`, `register()` 에서 refreshToken도 저장
- `logout()` 에서 refreshToken도 삭제
- return에 `refreshToken`, `setRefreshToken` 노출

---

### 2. `frontend/src/api/http.js`

#### 수정 전
```js
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
```

#### 수정 후
```js
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

// [추가] 동시 요청 시 중복 리프레시 방지용 큐
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
        originalRequest.headers.Authorization = `Bearer ${token}`;
        return http(originalRequest);
      });
    }

    originalRequest._retry = true;
    isRefreshing = true;

    try {
      const { data } = await axios.post(
        `${http.defaults.baseURL}/api/auth/refresh`,
        { refreshToken }
      );

      const newAccessToken = data?.data?.accessToken;
      const newRefreshToken = data?.data?.refreshToken;

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

// [추가] 인증 정보 초기화 및 로그인 페이지 리다이렉트
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
```

**변경 요약:**
- `isRefreshing`, `failedQueue` 추가 (동시 요청 시 중복 리프레시 방지)
- 401 발생 시 `/api/auth/refresh`로 토큰 갱신 시도
- 갱신 성공: 새 토큰 저장 후 원래 요청 재시도
- 갱신 실패: 인증 정보 삭제 후 로그인 페이지로 리다이렉트
- 인증 API(`login`, `register`, `refresh`)는 리프레시 대상에서 제외

---

## 동작 흐름

```
API 요청 → 401 발생
  ├─ refreshToken 존재?
  │   ├─ YES → POST /api/auth/refresh 호출
  │   │   ├─ 성공 → 새 토큰 저장, 원래 요청 재시도
  │   │   └─ 실패 → 토큰 삭제, /login으로 이동
  │   └─ NO → 토큰 삭제, /login으로 이동
  └─ 인증 API(login/register/refresh)면 → 리프레시 안 함
```