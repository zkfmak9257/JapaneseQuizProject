package com.team.jpquiz.member.command.infrastructure;

import com.team.jpquiz.member.command.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Refresh Token Repository
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 토큰 해시값으로 유효한 토큰 조회 (폐기되지 않은 토큰만)
     */
    Optional<RefreshToken> findByRefreshTokenHashAndRevokedFalse(String refreshTokenHash);

    /**
     * 사용자 ID로 모든 Refresh Token 폐기 (로그아웃, 탈퇴, 비밀번호 변경 시)
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :now WHERE rt.userId = :userId AND rt.revoked = false")
    void revokeAllByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    /**
     * 특정 토큰 폐기
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :now WHERE rt.refreshTokenHash = :tokenHash AND rt.revoked = false")
    void revokeByTokenHash(@Param("tokenHash") String tokenHash, @Param("now") LocalDateTime now);

    /**
     * 만료된 토큰 일괄 삭제 (스케줄러에서 호출, 정리용)
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);
}