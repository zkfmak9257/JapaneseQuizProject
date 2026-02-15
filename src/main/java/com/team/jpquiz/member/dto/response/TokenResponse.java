package com.team.jpquiz.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT 토큰 응답 DTO
 * 로그인 및 토큰 갱신 시 사용
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JWT 토큰 응답")
public class TokenResponse {

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "토큰 타입", example = "Bearer", defaultValue = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "액세스 토큰 만료 시간 (초)", example = "1800")
    private Long expiresIn;

    /**
     * 액세스 토큰만 포함하는 응답 생성 (토큰 갱신 시 사용)
     *
     * @param accessToken 액세스 토큰
     * @param expiresIn   만료 시간 (초)
     * @return TokenResponse
     */
    public static TokenResponse ofAccessToken(String accessToken, Long expiresIn) {
        return new TokenResponse(accessToken, null, "Bearer", expiresIn);
    }

    /**
     * 액세스 토큰과 리프레시 토큰을 모두 포함하는 응답 생성 (로그인/회원가입 시 사용)
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param expiresIn    액세스 토큰 만료 시간 (초)
     * @return TokenResponse
     */
    public static TokenResponse of(String accessToken, String refreshToken, Long expiresIn) {
        return new TokenResponse(accessToken, refreshToken, "Bearer", expiresIn);
    }
}
