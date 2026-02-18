package com.team.jpquiz.common.security;

import com.team.jpquiz.member.command.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증 유틸리티
 * Access Token, Refresh Token 생성 및 검증 담당
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    /**
     * 생성자: JWT 설정 값 주입 및 SecretKey 초기화
     *
     * @param secret                  Base64 인코딩된 JWT Secret Key
     * @param accessTokenExpiration   Access Token 만료 시간 (밀리초)
     * @param refreshTokenExpiration  Refresh Token 만료 시간 (밀리초)
     */
    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${app.jwt.refresh-token-expiration}") long refreshTokenExpiration) {

        // Base64 디코딩하여 SecretKey 생성
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Access Token 생성
     *
     * @param member 회원 엔티티
     * @return JWT Access Token
     */
    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(member.getUserId()))  // userId를 subject로 설정
                .claim("email", member.getEmail())            // email을 claim에 추가
                .claim("role", member.getRole().name())       // role을 claim에 추가
                .claim("type", "access")                      // 토큰 타입
                .issuedAt(now)                                // 발급 시간
                .expiration(expiryDate)                       // 만료 시간
                .signWith(secretKey)                          // 서명
                .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param member 회원 엔티티
     * @return JWT Refresh Token
     */
    public String generateRefreshToken(Member member) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(member.getUserId()))  // userId를 subject로 설정
                .claim("type", "refresh")                     // 토큰 타입
                .issuedAt(now)                                // 발급 시간
                .expiration(expiryDate)                       // 만료 시간
                .signWith(secretKey)                          // 서명
                .compact();
    }

    /**
     * 토큰 검증
     *
     * @param token JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 토큰에서 사용자 ID 추출
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 토큰에서 이메일 추출
     *
     * @param token JWT 토큰
     * @return 이메일 (Access Token만 포함)
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("email", String.class);
    }

    /**
     * 토큰에서 권한(Role) 추출
     *
     * @param token JWT 토큰
     * @return 권한 (Access Token만 포함)
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }

    /**
     * 토큰 타입 확인 (access 또는 refresh)
     *
     * @param token JWT 토큰
     * @return 토큰 타입
     */
    public String getTokenType(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("type", String.class);
    }

    /**
     * Refresh Token인지 확인
     *
     * @param token JWT 토큰
     * @return Refresh Token이면 true
     */
    public boolean isRefreshToken(String token) {
        return "refresh".equals(getTokenType(token));
    }

    /**
     * Access Token인지 확인
     *
     * @param token JWT 토큰
     * @return Access Token이면 true
     */
    public boolean isAccessToken(String token) {
        return "access".equals(getTokenType(token));
    }

    /**
     * 토큰 만료 시간 조회 (밀리초)
     *
     * @param token JWT 토큰
     * @return 만료 시간 (밀리초)
     */
    public long getExpirationTime(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date expiration = claims.getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

    /**
     * Access Token 만료 시간 조회 (초 단위)
     *
     * @return Access Token 만료 시간 (초)
     */
    public long getAccessTokenExpirationInSeconds() {
        return accessTokenExpiration / 1000;
    }

    /**
     * Refresh Token 만료 시간 조회 (초 단위)
     *
     * @return Refresh Token 만료 시간 (초)
     */
    public long getRefreshTokenExpirationInSeconds() {
        return refreshTokenExpiration / 1000;
    }

    /**
     * Refresh Token 만료 시간 조회 (밀리초 단위)
     *
     * @return Refresh Token 만료 시간 (밀리초)
     */
    public long getRefreshTokenExpirationInMillis() {
        return refreshTokenExpiration;
    }
}