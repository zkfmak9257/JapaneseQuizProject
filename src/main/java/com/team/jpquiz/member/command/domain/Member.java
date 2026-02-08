package com.team.jpquiz.member.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 회원 엔티티
 * users 테이블에 매핑
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (protected로 외부 생성 방지)
@AllArgsConstructor(access = AccessLevel.PROTECTED) // 빌더용 전체 필드 생성자
@Builder // 빌더 패턴 지원
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // 사용자 PK

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email; // 이메일 (중복 불가)

    @Column(name = "password", nullable = false, length = 255)
    private String password; // 비밀번호

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname; // 닉네임 (중복 불가)

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default // 빌더에서도 기본값 적용
    private MemberRole role = MemberRole.USER; // 권한 (기본값: USER)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default // 빌더에서도 기본값 적용
    private MemberStatus status = MemberStatus.ACTIVE; // 상태 (기본값: ACTIVE)

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt; // 탈퇴 일시

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt; // 마지막 로그인 일시

    @CreatedDate // JPA Auditing - 생성 시각 자동 설정
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 가입 일시

    @LastModifiedDate // JPA Auditing - 수정 시각 자동 업데이트
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정 일시

    /**
     * 회원 생성 정적 팩토리 메서드
     * 
     * @param email    이메일
     * @param password 암호화된 비밀번호
     * @param nickname 닉네임
     * @return 새로운 회원 엔티티
     */
    public static Member create(String email, String password, String nickname) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.nickname = nickname;
        member.role = MemberRole.USER;
        member.status = MemberStatus.ACTIVE;
        return member;
    }

    /**
     * 로그인 시각 업데이트
     */
    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 회원 탈퇴 처리
     */
    public void withdraw() {
        this.status = MemberStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
    }

    /**
     * 닉네임 변경
     * 
     * @param newNickname 새로운 닉네임
     */
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    /**
     * 비밀번호 변경
     * 
     * @param newPassword 새로운 암호화된 비밀번호
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * 관리자로 권한 변경
     */
    public void promoteToAdmin() {
        this.role = MemberRole.ADMIN;
    }

    /**
     * 일반 사용자로 권한 변경
     */
    public void demoteToUser() {
        this.role = MemberRole.USER;
    }

    /**
     * 활성 상태인지 확인
     * 
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

    /**
     * 관리자인지 확인
     * 
     * @return 관리자이면 true
     */
    public boolean isAdmin() {
        return this.role == MemberRole.ADMIN;
    }
}
