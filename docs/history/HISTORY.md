# Japanese Quiz

## 지금까지 진행한 큰 흐름
1) 프로젝트 초기 구조 세팅
- 패키지 구조 및 기본 모듈 구성

2) 공통 설정 골격 확정
- `application.yml` 공통값 정리
- `application-local.yml` 개인 설정 분리
- `application-local.yml.example`로 팀 공유
- `application-dev.yml` / `application-prod.yml` 기본 틀 추가

3) Swagger / Actuator 정책 정리
- 로컬/개발 ON, 운영 OFF 기준 확립

4) CORS 기본 정책 골격 추가
- YML에서 `app.cors.*`로 설정값 관리
- 로컬 허용 도메인 예시 추가(필요 시 수정)

5) Spring Boot 애플리케이션 엔트리포인트 확정
- 왜: 각자 기능 개발 전에 앱이 정상 부팅되는 최소 기준선 확보
- 어떻게: `JpQuizApplication`에 `@SpringBootApplication` + `main` 추가

6) MyBatis Mapper 기본 세팅 정리
- 왜: 부팅 시 mapper XML 파싱/매핑 오류 방지
- 어떻게: mapper XML 최소 뼈대 추가 + namespace를 interface FQCN과 일치
- 어떻게: mapper 인터페이스 스캔 설정(@Mapper 또는 @MapperScan) 확인

7) Security 기본 접근 정책 뼈대 확정
- 왜: 인증 기능 구현 전에도 "공개/인증 필요" 경계를 명확히 하고, 401/403 응답 흐름을 표준화하기 위해
- 어떻게: `SecurityConfig`에서 form/login, basic, CSRF 비활성화 + STATELESS 세션 + 공개 엔드포인트 permitAll + 나머지 authenticated
- 어떻게: 401/403 처리를 위한 EntryPoint/AccessDeniedHandler 연결 자리 확보(응답 포맷 구현은 인증 담당자 작업)

8) 공통 응답 포맷 및 예외 처리 확정
- 왜: 기능별 응답/에러 형식을 통일해 프론트 파싱과 에러 처리를 단순화하기 위해
- 어떻게: `ApiResponse`(성공), `ErrorResponse`(실패)로 표준 포맷 정의
- 어떻게: `ErrorCode` + `CustomException` + `GlobalExceptionHandler`로 전역 예외 흐름 구축

9) 보안 401/403 응답 포맷 통일
- 왜: 인증/인가 실패도 동일한 에러 포맷을 유지하기 위해
- 어떻게: `SecurityConfig`의 EntryPoint/AccessDeniedHandler에서 `ErrorResponse` 반환

10) JPA / MyBatis 기본 설정 마무리
- 왜: 각자 기능 개발 전에 저장소/쿼리 인프라를 안정적으로 준비하기 위해
- 어떻게: `@EnableJpaAuditing` 활성화, `@MapperScan` 범위 지정

11) Swagger 기본 설정 확정
- 왜: 로컬/개발에서는 문서 접근이 필요하고 운영에서는 비활성화가 필요하기 위해
- 어떻게: `local`, `dev` 프로필에서만 OpenAPI 그룹 활성화

12) Stats 패키지 충돌 복구
- 왜: `stats` 도메인 파일에 merge conflict marker가 남아 컴파일이 실패했기 때문
- 어떻게: `StatsController`, `StatsQueryService`, `StatsMapper`, `StatsMapper.xml` 충돌 마커 제거 및 정합성 정리
- 결과: `./gradlew compileJava` 통과

13) 프론트 Stats API 연동 (학습 랭킹 제외)
- 왜: 마이페이지/관리자 통계 화면이 더미 상태였고, 실제 운영 가능한 조회 흐름이 필요했기 때문
- 어떻게:
  - `GET /api/stats/me`
  - `GET /api/admin/stats/overview`
  - `GET /api/admin/stats/categories`
  - `GET /api/admin/stats/questions`
  - `GET /api/admin/stats/questions/top-wrong`
  연동 및 화면 렌더링 반영
- 어떻게: 공통 상태 컴포넌트(`LoadingState`, `ErrorState`, `EmptyState`) 적용
- 어떻게: `400/401/403` 메시지 분기 처리
- 범위 제외: `GET /api/admin/stats/rankings/learning`(학습 랭킹)

## 포트폴리오 핵심 개선 (요약)
1) 퀴즈 피드백 고도화 (3단계 학습 UX)
- 제출 후 흐름을 `결과 확인 -> 정답 확인 -> 해설 학습`으로 구조화
- WORD/SENTENCE 유형별 피드백 분기 및 오답 비교 가독성 개선
- 해설 패널에서 토큰 의미/문법 역할 표시를 지원해 학습 반복 효율 개선

2) 문장 토큰 메타데이터 백엔드 연동
- `quiz_sentence_tokens`에 `meaning_ko`, `grammar_role` 컬럼을 반영하고 응답 payload 확장
- `correctTokens`를 문자열 배열에서 객체 배열로 전환해 프론트/백엔드 스키마 동기화
- 제출 전 정답/해설 비노출 원칙 유지, 제출 후에만 학습 데이터 노출

3) 여행 회화 품질 개선 (실사용 맥락 정제)
- 영수증 표현을 일반 여행 상황에 맞게 `領収書` 중심에서 `レシート` 중심으로 정리
- 어색한 문장/토큰(요청형 표현, 후리가나 축약, 외침 표현 등) 보정 SQL 패치 작성 및 적용
- 데이터 패치 파일(`db/data_patch_receipt.sql`, `db/data_patch_travel_conversation.sql`)로 재현 가능한 정제 절차 확보

4) 시연 안정성 및 협업 품질 개선
- 내부망 시연용 API/CORS/Vite host 설정을 정리해 타기기 접속 데모 안정화
- 머지 충돌 마커로 인한 프런트 빌드 실패 구간 복구
- `gitignore` 정비로 `dist/.vite/node_modules/bin` 산출물 추적 문제 해소

## 추가 트러블슈팅 기록 (질문 기반)
1) 인증 만료/자동 갱신 점검
- 만료된 Access Token 로그(`Expired JWT token`)의 원인을 Refresh Token 회전/검증 흐름 관점으로 분석
- 프론트 인터셉터의 401 -> `/api/auth/refresh` -> 재시도 체인을 점검하고, 실패 시나리오(리프레시 토큰 부재/만료/폐기)를 정리

2) DB 스키마/연결 이슈 정리
- `Unknown column ... user_id` 유형 오류를 스키마 불일치 이슈로 확인하고 컬럼 정합성 점검
- 로컬 시연 환경에서 `JPQuiz` 스키마 기준으로 datasource 설정을 일치시키는 운영 체크포인트 반영

3) 퀴즈 진행 흐름 예외 보강
- 뒤로가기 후 재제출 시 `이미 제출` 오류로 진행이 막히는 UX 이슈를 복구 경로 중심으로 개선
- 제출 결과 캐시/상태 복원 로직을 보강해 재진입 시 학습 흐름이 끊기지 않도록 조정

4) 프런트 빌드/컴파일 장애 복구
- Vue SFC에 남은 머지 충돌 마커(`<<<<<<<`, `=======`, `>>>>>>>`)로 인한 파싱 실패를 다수 화면에서 정리
- 커스텀 엘리먼트 경고(`rb`)와 스타일/토큰 렌더링 이슈를 포함한 실행 안정성 보강
