# 기능 구현 계획 (Feature Implementation Plan)

## 프로젝트 구조 점검 (2026-02-18 기준)
- `src/main/java/com/team/jpquiz/quiz`는 CQRS(Command Query Responsibility Segregation, 명령-조회 책임 분리) 구조로 운영됩니다.
  - Command(명령): `quiz/command/**` (JPA 기반 저장/수정/삭제)
  - Query(조회): `quiz/query/**` (MyBatis 기반 조회/페이징/필터)
- 매퍼 XML은 `src/main/resources/mappers/quiz/**`로 분리되어 있으며, Query(조회) 서비스와 1:1로 매핑됩니다.
- 오답노트/즐겨찾기 컨트롤러는 Command(명령)와 Query(조회)를 분리해 구성되어 있습니다.
- 통계(`stats`)는 패키지/파일 스텁만 존재하며, 문제 신고(`problem report`) 도메인은 아직 패키지가 없습니다.

## 1. 오답노트 (Wrong Answer Note)
### 담당/리뷰
- 담당자(Owner): 정병진
- 리뷰어(Reviewer): 강성훈, 박찬진

### 목적
- 사용자가 틀린 문제를 자동으로 저장하고, 나중에 다시 풀어볼 수 있도록 하여 학습 효율을 높인다.

### 사용자 흐름(시나리오)
- 퀴즈 풀이 중 오답 발생 → 자동으로 오답노트에 저장 (또는 결과 화면에서 확인)
- 마이페이지/오답노트 메뉴 진입 → 오답 목록 조회
- '복습하기' 클릭 → 오답 문제들로 구성된 퀴즈 세트 생성
- 복습 중 정답 맞힘 → 오답노트에서 자동 삭제

### 요구사항
#### 필수
- [x] **API 정의**: 오답 저장(Upsert), 목록 조회(검색/필터), 삭제(해제), 재학습 세트 생성 API 구현
- [x] **DTO 설계**: `WrongAnswerSaveRequest`, `WrongAnswerResponse` 데이터 구조 구현
- [x] **Mapper/Repository**: `WrongAnswerMapper`(MyBatis) XML/인터페이스, `WrongAnswerRepository` 구현
- [x] **Service 구현**: 오답 저장(중복 시 갱신), 조회, 삭제 비즈니스 로직 구현

#### 선택(있으면 좋음)
- [x] 오답 노트 내 검색 기능 (키워드 검색)
- [x] 복습 시 부족한 문제 수를 랜덤 문제로 채우는 로직

### 권한 정책 (회원 / 비회원)
- 비회원(Guest) : 이용 불가 (로그인 유도, 401 Unauthorized)
- 회원 (User) : 본인의 오답노트만 이용 가능
- 막을 것(401 / 403 기준) : 타인의 오답노트 접근 시 403

### 화면(UI) 변경
- [ ] 페이지 / 컴포넌트 : 오답노트 목록 페이지
- [ ] 버튼 / 문구 : "복습 시작하기", "삭제"
- [ ] 이동 흐름 : 마이페이지 -> 오답노트

### API / DB 변경(선택)
- API : `POST /api/quiz/wrong-answers`, `GET /api/quiz/wrong-answers`, `DELETE /api/quiz/wrong-answers/{questionId}`, `POST /api/quiz/wrong-answers/review-set`
- DB : `wrong_answers` 테이블 활용

### 완료 조건
- [x] 틀린 문제가 DB에 저장/갱신(Upsert)되도록 구현
- [x] 오답 목록 조회(페이징 + fromDate/keyword/category 필터) 구현
- [x] 복습/일반 퀴즈 정답 시 오답노트 자동 삭제 로직 구현

---

## 2. 즐겨찾기 (Favorites)
### 담당/리뷰
- 담당자(Owner): 정병진
- 리뷰어(Reviewer): 강성훈, 박찬진

### 목적
- 사용자가 중요하다고 생각하거나 나중에 다시 보고 싶은 문제를 저장하여 관리한다.

### 사용자 흐름(시나리오)
- 문제 풀이 화면에서 '별표(즐겨찾기)' 아이콘 클릭 → 즐겨찾기 등록
- 다시 클릭 → 즐겨찾기 해제
- 마이페이지/즐겨찾기 메뉴에서 목록 확인

### 요구사항
#### 필수
- [x] **API 정의**: 등록/해제/토글/목록 조회 API 구현
- [x] **DTO 설계**: `FavoriteResponse`, `FavoriteToggleResponse` 구현 (요청은 Path Variable 기반)
- [x] **Mapper/Repository**: `FavoriteMapper` + `FavoriteRepository` 구현
- [x] **Service 구현**: 등록/해제/토글/목록 조회 로직 구현

#### 선택(있으면 좋음)
- [x] 카테고리별 즐겨찾기 필터링

### 권한 정책 (회원 / 비회원)
- 비회원(Guest) : 이용 불가 (401)
- 회원 (User) : 본인 즐겨찾기만 관리 가능

### 화면(UI) 변경
- [ ] 페이지 / 컴포넌트 : 즐겨찾기 목록 페이지
- [ ] 버튼 / 문구 : 문제 화면 내 '별표' 아이콘
- [ ] 이동 흐름 : 문제 풀이 화면 <-> 즐겨찾기 목록

### API / DB 변경(선택)
- API : `POST /api/favorites/{questionId}`, `DELETE /api/favorites/{questionId}`, `POST /api/favorites/{questionId}/toggle`, `GET /api/favorites`
- DB : `favorites` 테이블 활용

### 완료 조건
- [x] 즐겨찾기 등록 및 해제가 정상 동작하는지 확인
- [x] 목록 조회 시 등록한 문제만 조회되고 카테고리 필터가 적용되는지 확인

---

## 3. 문제 신고 (Problem Report)
### 담당/리뷰
- 담당자(Owner): 정병진
- 리뷰어(Reviewer): 강성훈, 박찬진

### 목적
- 문제의 오타, 정답 오류 등을 사용자가 신고하여 데이터 품질을 개선한다.

### 사용자 흐름(시나리오)
- 문제 풀이 중 오류 발견 → '신고' 버튼 클릭
- 신고 사유 선택 및 내용 입력 후 제출
- 관리자가 신고 내역 확인 후 처리 (문제 수정 또는 비활성화)

### 요구사항
#### 필수
- [ ] **API 정의**: 신고 제출 API, (관리자용) 신고 목록 조회 및 상태 변경 API
- [ ] **DTO 설계**: `ReportRequest`, `ReportResponse`
- [ ] **Mapper/Repository**: `ProblemReportMapper` 구현
- [ ] **Service 구현**: 신고 저장 로직, 상태 변경 로직

#### 선택(있으면 좋음)
- [ ] 신고 처리 시 사용자에게 알림 전송

### 권한 정책 (회원 / 비회원)
- 비회원(Guest) : 신고 가능 (단, 추적 제한될 수 있음)
- 회원 (User) : 신고 가능
- 관리자 (Admin) : 모든 신고 내역 조회 및 처리 가능

### 화면(UI) 변경
- [ ] 페이지 / 컴포넌트 : 문제 신고 모달/팝업, (관리자) 신고 관리 페이지
- [ ] 버튼 / 문구 : "문제 신고" 버튼
- [ ] 이동 흐름 : 문제 화면 -> 신고 팝업

### API / DB 변경(선택)
- API : `POST /api/reports`, `GET /api/admin/reports`, `PATCH /api/admin/reports/{reportId}`
- DB : `problem_reports` 테이블 활용

### 완료 조건
- [ ] 신고 데이터가 DB에 정상 저장되는지 확인
- [ ] 관리자가 상태를 변경할 수 있는지 확인

---

## 4. 통계 (Statistics)
### 담당/리뷰
- 담당자(Owner): 정병진
- 리뷰어(Reviewer): 강성훈, 박찬진

### 목적
- 학습 현황과 서비스 이용 지표를 시각화하여 제공한다.

### 사용자 흐름(시나리오)
- 관리자: 대시보드에서 전체 정답률, 오답률, 사용자 추이 확인
- 사용자: 마이페이지에서 나의 학습 진척도 확인

### 요구사항
#### 필수
- [ ] **API 정의**: 카테고리별/문제별 통계 조회, 유저별 학습량 조회 API
- [ ] **DTO 설계**: `StatsResponse`, `ChartDataDto`
- [ ] **Mapper/Repository**: `StatsMapper` (복잡한 집계 쿼리 작성)
- [ ] **Service 구현**: 통계 데이터 가공 및 조회 로직

#### 선택(있으면 좋음)
- [ ] 엑셀 다운로드 기능
- [ ] 캐싱 적용

### 권한 정책 (회원 / 비회원)
- 비회원(Guest) : 조회 불가
- 회원 (User) : 본인 통계만 조회
- 관리자 (Admin) : 전체 통계 조회

### 화면(UI) 변경
- [ ] 페이지 / 컴포넌트 : 통계 차트 (Chart.js 등 활용 예상)
- [ ] 버튼 / 문구 : "통계 보기"
- [ ] 이동 흐름 : 관리자 페이지 -> 통계

### API / DB 변경(선택)
- API : `GET /api/stats/**`, `GET /api/admin/stats/**`
- DB : 기존 테이블 집계 활용

### 완료 조건
- [ ] 집계 데이터가 정확한지 검증 (수동 계산과 비교)
- [ ] 권한에 따른 접근 제어 확인

---

## 현재 구현 상태 브리핑 (2026-02-18 기준)

### 오답노트
- `WrongAnswer` Entity / Repository / Service / Mapper / Controller 구현 완료
- Upsert 저장 로직 구현 완료
- 목록 조회(페이징 + fromDate/keyword/category 필터) 구현 완료
- 재학습 세트 생성(10문제, 부족분 랜덤 보충) 구현 완료
- 정답 시 자동 제거(퀴즈 채점 연동) 구현 완료
- 인증 사용자 연동(`SecurityUtil`, `@AuthenticationPrincipal`) 적용 완료

### 즐겨찾기
- `Favorite` Entity / Repository / Service / Controller 구현 완료
- 등록 API(`POST /api/favorites/{questionId}`), 해제 API(`DELETE /api/favorites/{questionId}`), 토글 API(`POST /api/favorites/{questionId}/toggle`) 구현됨
- 목록 조회 API(`GET /api/favorites`) 구현 완료
- 카테고리 필터링 구현 완료
- `favorites(member_id, question_id)` 유니크 제약으로 중복 등록 방지 적용됨

### 문제 신고
- 도메인/DTO/Mapper/Service/Controller 모두 미구현

### 통계
- `StatsController`, `StatsQueryService`, `StatsMapper`, `StatsResponse` 스텁(빈 구현) 상태

## 다음 단계 실행 계획 (한 단계씩)

### Step 1. 오답노트/즐겨찾기 실사용 전환
- 상태: `완료`

### Step 2. 퀴즈 채점 연동 자동화
- 상태: `완료`

### Step 3. 즐겨찾기 CUD + 목록
- 상태: `완료`

### Step 4. 문제 신고 C + 관리자 처리 (현재 우선순위)
- 목표: 데이터 품질 피드백 루프 완성
- 작업:
  1. 신고 등록 API(`POST /api/reports`) 구현
  2. 관리자 신고 목록 API(`GET /api/admin/reports`) 구현
  3. 관리자 신고 처리 API(`PATCH /api/admin/reports/{reportId}`) 구현
  4. 상태 변경 이력(`action`) 저장 규칙 정의

### Step 5. 통계 API 최소 2종
- 목표: 개인 통계 1종 + 관리자 통계 1종 우선 제공
- 작업:
  1. 개인 통계 API 1종(`GET /api/stats/me`) 우선 구현
  2. 관리자 통계 API 1종(`GET /api/admin/stats/overview`) 구현
  3. 집계 기준(first/latest) 쿼리 분리 및 검증
