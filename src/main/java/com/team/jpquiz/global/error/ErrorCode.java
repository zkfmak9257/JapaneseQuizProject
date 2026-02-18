package com.team.jpquiz.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "요청이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    ATTEMPT_NOT_FOUND(HttpStatus.NOT_FOUND, "ATTEMPT_NOT_FOUND", "퀴즈 시도를 찾을 수 없습니다."),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND", "해당 순번의 문제를 찾을 수 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다."),

    // Auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "DUPLICATE_NICKNAME", "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "INACTIVE_MEMBER", "비활성화된 계정입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "만료된 토큰입니다."),
    CURRENT_PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "CURRENT_PASSWORD_REQUIRED", "현재 비밀번호를 입력해주세요."),
    REFRESH_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN_REQUIRED", "리프레시 토큰은 필수입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "유효하지 않은 리프레시 토큰입니다."),
    ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "ALREADY_WITHDRAWN", "이미 탈퇴한 회원입니다."),

    // Quiz
    GUEST_QUIZ_LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "GUEST_QUIZ_LIMIT_EXCEEDED", "비회원은 퀴즈를 1개만 풀 수 있습니다. 로그인 후 이용해주세요."),

    // Problem Report
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT_NOT_FOUND", "신고 내역을 찾을 수 없습니다."),
    INVALID_REPORT_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "INVALID_REPORT_STATUS_TRANSITION", "신고 상태 전이가 올바르지 않습니다."),
    REPORT_TARGET_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT_TARGET_QUESTION_NOT_FOUND", "신고 대상 문제를 찾을 수 없습니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus(){
        return status;
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}
