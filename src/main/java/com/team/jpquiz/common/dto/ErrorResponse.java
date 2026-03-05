package com.team.jpquiz.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "공통 에러 응답")
public class ErrorResponse {
    @Schema(description = "에러 코드", example = "INVALID_REQUEST")
    private final String code;
    @Schema(description = "에러 메시지", example = "요청이 올바르지 않습니다.")
    private final String message;
    @Schema(description = "HTTP 상태 코드", example = "400")
    private final int status;
    @Schema(description = "요청 경로", example = "/api/quiz/attempts/1001/answers")
    private final String path;
    @Schema(description = "응답 시각(UTC ISO-8601)", example = "2026-03-02T10:25:08.798088Z")
    private final String timestamp;

    private ErrorResponse(String code, String message, int status, String path, String timestamp) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
    }

    public static ErrorResponse of(String code, String message, int status, String path) {
        return new ErrorResponse(code, message, status, path, Instant.now().toString());
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
