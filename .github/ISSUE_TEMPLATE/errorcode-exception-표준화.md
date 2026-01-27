---
name: ErrorCode/Exception 표준화
about: Describe this issue template's purpose here.
title: ''
labels: ''
assignees: ''

---

내용(추천)

  ## 담당/리뷰
  - 담당자(Owner): 강성훈
  - 리뷰어(Reviewer): 정병진, 박찬진

  ## 목적
  - 각자 기능 구현 시 통일된 응답/예외 처리 기준을 확보.

  ## 요구사항
  ### 필수
  - [ ] 에러 응답 포맷 확정: code / message / status / path / timestamp
  - [ ] ErrorCode 네이밍 규칙 확정: DOMAIN_ACTION
  - [ ] HTTP status 매핑 기준 확정: 400/401/403/404/409/500
  - [ ] 전역 예외 처리 기본 골격 작성(CustomException, ErrorCode, GlobalExceptionHandler)
  - [ ] 공통 응답 DTO(ApiResponse) 기본 골격 작성

  ### 선택(있으면 좋음)
  - [ ] Validation 오류 응답 포맷 예시 추가
  - [ ] 문서/README에 규칙 기록

  ## 권한 정책 ( 회원 / 비회원)
  - 비회원(Guest) : 해당 없음
  - 회원 (User) : 해당 없음
  - 막을 것(401 / 403 기준) : Security 기본 정책 이슈에서 결정

  ## 화면(UI) 변경
  - [ ] 페이지 / 컴포넌트 : 없음
  - [ ] 버튼 / 문구 : 없음
  - [ ] 이동 흐름 : 없음

  ## API / DB 변경(선택)
  - API : 공통 응답/예외 스펙 확정
  - DB : 없음

  ## 완료 조건
  - [ ] 기능 동작 확인(샘플 예외 응답 확인)
  - [ ] 예외 케이스(400 / 401 / 403) 확인(간단 샘플)
  - [ ] 간단 테스트(수동 체크)
  - [ ] (선택) 문서 / README 업데이트

  ## 결정/합의가 필요한 것(있으면)
  - 예) ErrorCode 메시지 언어(한글/영문)
  - 예) timestamp 포맷(ISO-8601)
