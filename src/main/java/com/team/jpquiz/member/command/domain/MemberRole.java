package com.team.jpquiz.member.command.domain;

/**
 * 회원 역할 Enum
 * DB의 ENUM('USER', 'ADMIN')에 매핑
 */
public enum MemberRole {
    USER, // 일반 사용자
    ADMIN // 관리자
}
