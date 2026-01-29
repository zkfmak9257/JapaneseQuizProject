package com.team.jpquiz.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

// null 필드는 JSON에서 제외하도록
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success; // 응답 성공 여부
    private final String code; // 성공 / 실패시 코드
    private final String message; // 사용자와 프론트가 읽을 메시지
    private final T data; // payload
    private final String timestamp; //응답 생성 시각(문자열 ISO-8601)

    // 생성자 생성, 정적 팩토리 메서드만 사용하도록 설계
    private ApiResponse(boolean success, String code, String message, T data, String timestamp) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;

    }

    // 가장 기본 성공 응답 생성
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "SUCCESS", "OK", data, Instant.now().toString());
    }

    // 성공 커스터마이징
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, "SUCCESS", message, data, Instant.now().toString());
    }
    // 데이터 없는 성공 응답.
    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, "SUCCESS", "OK", null, Instant.now().toString());
    }
    // 실패 응답 생성
    public static ApiResponse<Void> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null, Instant.now().toString());
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }
}






