# Japanese Quiz

## 지금까지 진행한 큰 흐름
1) 프로젝트 초기 구조 세팅

- 패키지 구조 및 기본 모듈 구성
- 
## 2) 공통 설정 골격 확정

- `application.yml` 공통값 정리

- `application-local.yml` 개인 설정 분리

- `application-local.yml.example`로 팀 공유

- `application-dev.yml` / `application-prod.yml` 기본 틀 추가

## 3) Swagger / Actuator 정책 정리

- 로컬/개발 ON, 운영 OFF 기준 확립

## 4) CORS 기본 정책 골격 추가

- YML에서 `app.cors.*`로 설정값 관리

- 로컬 허용 도메인 예시 추가(필요 시 수정)

## 5) Spring Boot 애플리케이션 엔트리포인트 확정

- 왜: 각자 기능 개발 전에 앱이 정상 부팅되는 최소 기준선 확보

- 어떻게: `JpQuizApplication`에 `@SpringBootApplication` + `main` 추가

## 6) MyBatis Mapper 기본 세팅 정리

- 왜: 부팅 시 mapper XML 파싱/매핑 오류 방지

- 어떻게: mapper XML 최소 뼈대 추가 + namespace를 interface FQCN과 일치

- 어떻게: mapper 인터페이스 스캔 설정(@Mapper 또는 @MapperScan) 확인

## 7) Security 기본 접근 정책 뼈대 확정

- 왜: 인증 기능 구현 전에도 "공개/인증 필요" 경계를 명확히 하고, 401/403 응답 흐름을 표준화하기 위해

- 어떻게: `SecurityConfig`에서 form/login, basic, CSRF 비활성화 + STATELESS 세션 + 공개 엔드포인트 permitAll + 나머지 authenticated

- 어떻게: 401/403 처리를 위한 EntryPoint/AccessDeniedHandler 연결 자리 확보(응답 포맷 구현은 인증 담당자 작업)

---