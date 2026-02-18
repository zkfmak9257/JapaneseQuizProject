package com.team.jpquiz.member.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Refresh Token 엔티티
 * 서버에서 발급한 Refresh Token을 해시로 저장하여 폐기(revocation) 가능하게 함
 */
@Entity
@Table(name = "auth_refresh_tokens")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "refresh_token_hash", nullable = false, length = 255)
    private String refreshTokenHash;

    @Column(name = "is_revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private RefreshToken(Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        this.userId = userId;
        this.refreshTokenHash = refreshTokenHash;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    /**
     * Refresh Token 생성
     *
     * @param userId           사용자 ID
     * @param refreshTokenHash 토큰 해시값
     * @param expiresAt        만료 시각
     * @return RefreshToken 엔티티
     */
    public static RefreshToken create(Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        return new RefreshToken(userId, refreshTokenHash, expiresAt);
    }

    /**
     * 토큰 폐기 처리
     */
    public void revoke() {
        this.revoked = true;
        this.revokedAt = LocalDateTime.now();
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @return 만료되었으면 true
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    /**
     * 토큰 유효 여부 확인 (폐기되지 않고, 만료되지 않음)
     *
     * @return 유효하면 true
     */
    public boolean isValid() {
        return !this.revoked && !isExpired();
    }
}