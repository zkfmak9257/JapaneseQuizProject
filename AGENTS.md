# AGENTS.md

이 문서는 `JapaneseQuizProject`에서 작업하는 에이전트용 실행 규칙입니다.

## 1) 언어/소통 규칙
- 모든 사고, 설명, 계획, 비교는 한국어로 작성합니다.
- 영어 용어는 `영어 원문 + 한국어 의미`를 함께 표기합니다.
  - 예: Dependency Injection(DI, 의존성 주입)
- 항상 존댓말을 사용하고, 근거 중심으로 명확히 설명합니다.

## 2) 설명 방식 규칙
- 설명은 아래 순서를 기본으로 따릅니다.
  1. 전체 개념 요약
  2. 왜 필요한지(문제 -> 해결)
  3. 구조 설명
  4. 코드 설명
  5. 실무 장단점
- 축약 설명(예: "그냥 이렇게 하면 됩니다")은 금지합니다.

## 3) 코드 작성 규칙
- 실무 기준과 학습 친화성을 동시에 만족하도록 작성합니다.
- 기존 코드가 있을 경우 구조/흐름을 최대한 유지하고 필요한 부분만 최소 수정합니다.
- 변경 시 변경 이유를 명확히 남깁니다.
- 매직 코드(Magic Code, 의미 없는 하드코딩 값) 사용을 지양합니다.

## 4) 프로젝트 기술 맥락
- Language: Java 17
- Framework: Spring Boot 3.5.10
- Database: MariaDB
- Data Access: JPA + MyBatis (CQRS: Write=JPA, Read=MyBatis)
- Security: Spring Security
- Docs/Monitoring: Swagger(OpenAPI), Actuator

## 5) 프로젝트 컨벤션 요약
- Controller는 Entity를 직접 반환하지 않고 DTO를 반환합니다.
- 예외는 `CustomException(ErrorCode)` + `GlobalExceptionHandler` 패턴을 사용합니다.
- 네이밍:
  - Java 클래스: PascalCase
  - 메서드/변수: lowerCamelCase
  - DB 객체: snake_case
  - 상수/Enum: UPPER_SNAKE_CASE
- 메서드 어휘:
  - `find`: 단건 조회
  - `findAll`/`findList`: 목록 조회
  - `search`: 조건 검색
  - `exists`: 존재 여부
  - `save`/`update`/`delete`: 명확한 의도 기반 동작

## 6) 아키텍처 설명 시 필수 항목
- 단일 책임 원칙(SRP, Single Responsibility Principle)
- 계층 분리 이유
- 확장 시 변경 포인트
- 레거시 방식 vs 모던 방식 비교 및 선택 근거

## 7) 테스트/운영 관점
- 테스트 코드가 필요한 변경은 테스트 전략을 함께 제안합니다.
- 성능/확장성/유지보수 관점 코멘트를 최소 1줄 포함합니다.
- 실무에서 자주 발생하는 이슈가 예상되면 사전 경고를 제공합니다.

## 8) 최종 점검 질문
- "지금 설명이 사용자의 성장에 실제로 도움이 되는가?"를 기준으로 답변 품질을 점검합니다.
