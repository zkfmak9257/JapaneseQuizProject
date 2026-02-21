# 프론트엔드 요구사항 명세서

- 버전: 1.0
- 작성일: 2026-02-21
- 상태: 초안

---

## 1. 라우팅 목록

| # | 경로 | View 파일 | 페이지 설명 | 인증 필요 |
|---|------|-----------|------------|----------|
| 1 | `/` | `MainView.vue` | 메인 페이지 | ❌ |
| 2 | `/login` | `LoginView.vue` | 로그인 | ❌ |
| 3 | `/signup` | `SignupView.vue` | 회원가입 | ❌ |
| 4 | `/quiz/start` | `QuizStartView.vue` | 퀴즈 시작 | ❌ |
| 5 | `/quiz/attempts/:attemptId/questions/:seq` | `QuizSolveView.vue` | 퀴즈 풀기 — 문제 풀이 화면 | ❌ |
| 6 | `/quiz/attempts/:attemptId/result` | `QuizResultView.vue` | 퀴즈 결과 — 점수/정답 확인 | ❌ |
| 7 | `/mypage` | `MyPageView.vue` | 마이페이지 — 프로필/통계 대시보드 | ✅ |
| 8 | `/me/history` | `MeHistoryListView.vue` | 학습 기록 목록 | ✅ |
| 9 | `/me/history/:attemptId` | `MeHistoryDetailView.vue` | 학습 기록 상세 | ✅ |
| 10 | `/me/stats` | `MeStatsView.vue` | 내 통계 | ✅ |
| 11 | `/me/profile` | `MeProfileView.vue` | 프로필 수정 | ✅ |
| 12 | `/quiz/favorites` | `FavoriteListView.vue` | 즐겨찾기 목록 | ✅ |
| 13 | `/quiz/wrong-answers` | `WrongAnswerListView.vue` | 오답노트 | ✅ |
| 14 | `/admin` | `AdminDashboardView.vue` | 관리자 대시보드 | ✅ (Admin) |
| 15 | `/admin/reports` | `AdminReportsView.vue` | 관리자 신고 관리 | ✅ (Admin) |
| 16 | `/admin/stats` | `AdminStatsView.vue` | 관리자 통계 | ✅ (Admin) |
| 17 | `/403` | `ForbiddenView.vue` | 접근 금지 (403 에러) | ❌ |


## 성훈
### 4, 5, 6, 상황선택 목록 추가
## 찬진
### 2, 3, 14, 15, 16, 17
## 병진
### 1, 7, 8, 9, 10, 11, 12, 13

---

## 2. 페이지별 요구사항


### 2-1. MainView — `/`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
- 최상단 검색어 기능 구현
- 마이페이지 토글로 학습기록 즐겨찾기 오답노트 표시
- 상황선택 추가 시 레이아웃 수, 애니매이션 고려하여 더보기 버튼 구현
- 이모지 변경
- 타이틀 명 변경 및 파비콘 변경
-

**비고**

-

---
### 2-2. LoginView — `/login`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---
### 2-3. SignupView — `/signup`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---
### 2-4. QuizStartView — `/quiz/start`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---
### 2-5. QuizSolveView — `/quiz/attempts/:attemptId/questions/:seq`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---
### 2-6. QuizResultView — `/quiz/attempts/:attemptId/result`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-7. MyPageView — `/mypage`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-8. MeHistoryListView — `/me/history`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-9. MeHistoryDetailView — `/me/history/:attemptId`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-10. MeStatsView — `/me/stats`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-11. MeProfileView — `/me/profile`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-12. FavoriteListView — `/quiz/favorites`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-13. WrongAnswerListView — `/quiz/wrong-answers`

**인증:** 필요 (로그인)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-14. AdminDashboardView — `/admin`

**인증:** 필요 (Admin)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-15. AdminReportsView — `/admin/reports`

**인증:** 필요 (Admin)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-16. AdminStatsView — `/admin/stats`

**인증:** 필요 (Admin)

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

### 2-17. ForbiddenView — `/403`

**인증:** 불필요

**UI 구성**
-

**기능 요구사항**
-

**비고**
-

---

## 3. 공통 컴포넌트

| 컴포넌트 | 설명 | UI 구성 | 비고 |
|---------|------|---------|------|
| AppHeader / NavBar | 상단 공통 헤더 | | |
| AppFooter | 하단 공통 푸터 | | |
| LoadingSpinner | 로딩 상태 표시 | | |
| ErrorMessage | 에러 메시지 표시 | | |
| Modal / Dialog | 팝업 다이얼로그 | | |
| Pagination | 목록 페이지네이션 | | |
| QuizCard | 퀴즈 문제 카드 | | |

---

## 4. 개정 이력

| 버전 | 날짜 | 작성자 | 변경 내용 |
|------|------|--------|----------|
| 1.0 | 2026-02-21 | | 최초 작성 |
