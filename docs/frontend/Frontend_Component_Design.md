# Frontend Component Design (Vue)

## 목적
- `UIUX_Spec.md`, `Wireframe.md`를 실제 Vue 구현 단위로 변환한다.
- 페이지별 상태(state), 이벤트(event), API 호출 시점을 명확히 한다.

## 공통 구조
- 레이아웃: `App.vue`
- 라우팅: `src/router/index.js`
- API 모듈: `src/api/*`
- 상태관리: Pinia 사용 권장 (`authStore`, `quizStore`)

## 페이지/컴포넌트 설계

## 1) 메인 페이지 (`/`)
### 컴포넌트
- `MainPageView`
- `CategorySelector`
- `TopAuthActions`

### 상태
- `selectedCategory: string | null`
- `isLoggedIn: boolean`

### 이벤트
- `onSelectCategory(category)`
- `onClickStart()`

### API
- 없음 (정책에 따라 카테고리 API 추가 가능)

---

## 2) 로그인 페이지 (`/login`)
### 컴포넌트
- `LoginView`
- `LoginForm`

### 상태
- `email: string`
- `password: string`
- `loading: boolean`
- `errorMessage: string`

### 이벤트
- `onSubmitLogin()`

### API
- `POST /api/auth/login`

---

## 3) 퀴즈 시작 페이지 (`/quiz/start`)
### 컴포넌트
- `QuizStartView`
- `CountSelector`

### 상태
- `count: number` (기본 10)
- `category: string | null` (정책 확정 시)

### 이벤트
- `onClickStartQuiz()`

### API
- `POST /api/quiz/attempts/start`

---

## 4) 퀴즈 풀이 페이지 (`/quiz/attempts/:attemptId/questions/:seq`)
### 컴포넌트
- `QuizSolveView`
- `QuizProgress`
- `QuestionCard`
- `ChoiceList`
- `ReportButton`

### 상태
- `attemptId: number`
- `seq: number`
- `question: object | null`
- `selectedChoiceId: number | null`
- `submitting: boolean`
- `showGuestModal: boolean`

### 이벤트
- `onSelectChoice(choiceId)`
- `onSubmitAnswer()`
- `onClickReport()`
- `onClickComplete()`

### API
- `GET /api/quiz/attempts/{attemptId}/questions/{seq}`
- `POST /api/quiz/attempts/{attemptId}/answers`
- `POST /api/quiz/attempts/{attemptId}/complete`

---

## 5) 결과 페이지 (`/quiz/attempts/:attemptId/result`)
### 컴포넌트
- `QuizResultView`
- `ResultSummaryCard`
- `ResultActionGroup`

### 상태
- `result: object | null`
- `loading: boolean`

### 이벤트
- `onClickRetry()`
- `onClickWrongAnswers()`
- `onClickNextQuiz()`

### API
- `GET /api/quiz/attempts/{attemptId}/result`

---

## 6) 마이페이지 (`/mypage`)
### 컴포넌트
- `MyPageView`
- `ProfileEditCard`
- `StatsSummaryCard`
- `MyPageQuickActions`

### 상태
- `memberProfile`
- `statsSummary`

### 이벤트
- `onUpdateProfile()`
- `onClickGoWrongAnswers()`
- `onClickGoStats()`

### API
- `GET /api/members/me`
- `PATCH /api/members/me`
- `GET /api/stats/me`

---

## 8) 관리자 통계 (`/admin/stats`)
### 컴포넌트
- `AdminStatsView`
- `AdminOverviewCards`
- `CategoryAccuracyList`
- `QuestionStatsList`
- `TopWrongList`

### 상태
- `basis: 'latest' | 'first'`
- `page: number`
- `size: number`
- `limit: number`
- `overview`
- `categories[]`
- `questionPage`
- `topWrongItems[]`

### 이벤트
- `onChangeBasis()`
- `onChangeQuestionPage()`
- `onChangeQuestionSize()`
- `onChangeTopWrongLimit()`
- `onRetrySection()`

### API
- `GET /api/admin/stats/overview`
- `GET /api/admin/stats/categories?basis=latest|first`
- `GET /api/admin/stats/questions?basis=&page=&size=`
- `GET /api/admin/stats/questions/top-wrong?limit=`
- 제외: `GET /api/admin/stats/rankings/learning` (타 이슈 담당)

---

## 7) 오답노트 (`/mypage/wrong-answers`)
### 컴포넌트
- `WrongAnswerListView`
- `WrongAnswerList`
- `ReviewSetButton`

### 상태
- `items: array`
- `page: number`
- `size: number`

### 이벤트
- `onChangePage()`
- `onClickStartReviewSet()`

### API
- `GET /api/quiz/wrong-answers`
- `POST /api/quiz/wrong-answers/review-set`

---

## 에러 처리 표준
- `400`: 입력/상태 오류 안내
- `401`: 로그인 페이지로 이동 + 안내
- `403`: 권한 없음 안내
- `404`: 대상 없음 안내
- 공통 컴포넌트 권장: `ErrorBanner`

## 구현 순서 권장
1. 로그인/토큰 처리
2. 퀴즈 시작/풀이/제출
3. 완료/결과 조회
4. 오답노트/즐겨찾기
5. 마이페이지/통계
