package com.team.jpquiz.global.security;

import com.team.jpquiz.member.command.domain.Member;
import com.team.jpquiz.member.command.domain.MemberRole;
import com.team.jpquiz.member.command.domain.MemberStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security UserDetails 구현체
 * Member 엔티티를 Spring Security가 이해할 수 있는 형태로 래핑
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final MemberRole role;
    private final MemberStatus status;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * private 생성자 - 정적 팩토리 메서드로만 생성
     */
    private UserPrincipal(
            Long userId,
            String email,
            String password,
            MemberRole role,
            MemberStatus status,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.authorities = authorities;
    }

    /**
     * Member 엔티티로부터 UserPrincipal 생성
     *
     * @param member Member 엔티티
     * @return UserPrincipal 인스턴스
     */
    public static UserPrincipal from(Member member) {
        // MemberRole을 GrantedAuthority로 변환
        // Spring Security는 "ROLE_" 접두사를 관례로 사용
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole().name());

        return new UserPrincipal(
                member.getUserId(),
                member.getEmail(),
                member.getPassword(),
                member.getRole(),
                member.getStatus(),
                Collections.singletonList(authority)
        );
    }

    // ========== UserDetails 인터페이스 구현 ==========

    /**
     * 사용자 식별자 반환 (Spring Security 표준)
     * 이메일을 username으로 사용
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 암호화된 비밀번호 반환
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 권한 목록 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 계정 만료 여부
     * 현재 프로젝트에서는 만료 개념 없음
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * MemberStatus가 ACTIVE일 때만 잠기지 않음
     */
    @Override
    public boolean isAccountNonLocked() {
        return status == MemberStatus.ACTIVE;
    }

    /**
     * 비밀번호 만료 여부
     * 현재 프로젝트에서는 비밀번호 만료 개념 없음
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * MemberStatus가 ACTIVE일 때만 활성화
     */
    @Override
    public boolean isEnabled() {
        return status == MemberStatus.ACTIVE;
    }
}