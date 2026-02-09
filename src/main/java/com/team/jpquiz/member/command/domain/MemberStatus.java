package com.team.jpquiz.member.command.domain;

/**
 * 회원 상태 Enum
 * DB의 ENUM('ACTIVE', 'WITHDRAWN')에 매핑
 */
public enum MemberStatus {
    ACTIVE, // 활성 상태
    WITHDRAWN // 탈퇴 상태
}
