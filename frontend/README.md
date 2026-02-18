# Frontend (Vue)

## Start
1. `cd frontend`
2. `npm install`
3. `.env.example`를 복사해 `.env` 생성
4. `npm run dev`

## Domain-based Structure
- `src/api/authApi.js`
- `src/api/quizApi.js`
- `src/api/favoriteApi.js`
- `src/api/wrongAnswerApi.js`
- `src/views/*`

## Notes
- 백엔드 공통 응답 포맷(`ApiResponse`) 기준으로 `data.data`를 언랩합니다.
- 기본 API 주소는 `VITE_API_BASE_URL`입니다.
