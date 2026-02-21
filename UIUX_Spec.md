# UI/UX Spec (Quiz Domain)

## 목적
- 퀴즈 도메인 화면 흐름과 페이지 역할을 먼저 고정해 프론트 구현 기준선을 만든다.
- 백엔드 API 구현 상태(시작/문제조회/제출/완료/결과조회)에 맞춘 사용자 동선을 정의한다.

## 프로젝트 확정 정책 (2026-02-19)
1. 엔드포인트 기준
- 퀴즈 플로우: `/api/quiz/...`
- 즐겨찾기: 표준 경로 `/api/favorites` 사용
- 마이페이지 데이터: `/api/members/me`, `/api/stats/me`

2. Guest 제한 코드
- 게스트 제한은 `403` + `GUEST_QUIZ_LIMIT_EXCEEDED` 기준으로 처리

3. 결과 데이터 출처
- 결과 화면은 항상 `GET /api/quiz/attempts/{attemptId}/result` 기준으로 렌더링
- 로컬 누적 데이터는 결과 화면의 기준 데이터로 사용하지 않음

4. 마이페이지 라우트
- 현재 라우트는 `/mypage` 유지
- `/me/*` 구조 전환은 별도 리팩토링 이슈로 분리

5. 신고 API
- Guest/User 공통 `POST /api/reports`
- 요청 필드: `questionId`, `reportType`, `content`

6. 공통 에러 UX
- `400`: 요청값 오류
- `401`: 로그인 필요
- `403`: 권한/정책 제한
- `404`: 대상 없음
- `409`: 중복/상태 충돌
- `500`: 서버 오류, 재시도 유도

## 구현 반영 상태 (2026-02-21)
1. 개인 통계
- `/me/stats`에서 `GET /api/stats/me` 연동 완료
- 로딩/에러/빈 상태 UI 반영 완료

2. 관리자 통계
- `/admin/stats`에서 아래 API 연동 완료
  - `GET /api/admin/stats/overview`
  - `GET /api/admin/stats/categories`
  - `GET /api/admin/stats/questions`
  - `GET /api/admin/stats/questions/top-wrong`
- 필터/페이지네이션(basis, page, size, limit) UI 반영 완료

3. 범위 제외
- `GET /api/admin/stats/rankings/learning`(학습 랭킹)은 타 이슈 담당으로 제외

## 핵심 사용자 흐름
1. 메인 페이지 진입
2. 카테고리 선택
3. 퀴즈 시작
4. 문제 풀이(문제 + 보기 4개 + 신고하기)
5. 퀴즈 종료 후 사용자 상태별 분기
6. 마이페이지/오답노트/통계 이동

## 페이지별 요구사항

## 1) 메인 페이지
- 중앙: 카테고리 선택 영역
- 우측 상단: `로그인`, `회원가입` 버튼
- 메인 CTA: `퀴즈 시작`
- 정책 분기:
  - 비회원 시작 허용 시: 바로 퀴즈 시작 가능
  - 비회원 시작 제한 시: 로그인 유도

## 2) 퀴즈 화면
- 필수 요소:
  - 문제 문장
  - 보기 4개
  - 제출 버튼
  - 진행률(`현재 번호 / 전체`)
- 우측 상단:
  - `신고하기` 버튼
- 동작:
  - 제출 성공 시 다음 문제로 이동
  - 마지막 문제 제출 후 완료 단계로 이동

## 3) 퀴즈 종료 분기
- 비회원:
  - 모달 노출: `회원가입` / `로그인` 유도
- 회원:
  - 액션 버튼 노출:
    - `다음 문제`(해당 플로우 정책 시)
    - `다시 풀기`
    - `틀린 문제 보기`
  - `틀린 문제 보기` 클릭 시 `/quiz/wrong-answers` 이동

## 4) 마이페이지
- 회원 정보 수정
- 오답노트 진입
- 통계 요약 영역

## 5) 오답노트 페이지
- 틀린 문제 목록
- 재학습 시작 진입 버튼

## 6) 통계 영역
- 마이페이지 내 탭 또는 별도 페이지로 제공
- 최소 노출 항목:
  - 정답률
  - 누적 풀이 수
  - 오답 비중

## 공통 UX 원칙
- 1페이지 1주요 액션 원칙(Primary 버튼 1개)
- 에러 코드별 고정 메시지 사용
  - 400: 요청 오류
  - 401: 로그인 필요
  - 403: 권한 없음
  - 404: 대상 없음
- 로딩/빈 상태/오류 상태를 모든 리스트 페이지에 표시

## 라우트 제안
- `/`
- `/login`
- `/signup`
- `/quiz/start`
- `/quiz/attempts/:attemptId/questions/:seq`
- `/quiz/attempts/:attemptId/result`
- `/mypage`
- `/quiz/wrong-answers`
- `/quiz/favorites`
