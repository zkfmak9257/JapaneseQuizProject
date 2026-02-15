package com.team.jpquiz.member.dto.response;

import com.team.jpquiz.member.command.domain.Member;
import com.team.jpquiz.member.command.domain.MemberRole;
import com.team.jpquiz.member.command.domain.MemberStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 정보 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 정보 응답")
public class MemberResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "일본어마스터")
    private String nickname;

    @Schema(description = "권한", example = "USER")
    private MemberRole role;

    @Schema(description = "상태", example = "ACTIVE")
    private MemberStatus status;

    @Schema(description = "마지막 로그인 일시", example = "2026-02-10T19:30:00")
    private LocalDateTime lastLoginAt;

    @Schema(description = "가입 일시", example = "2026-02-01T10:00:00")
    private LocalDateTime createdAt;

    /**
     * Member 엔티티를 MemberResponse로 변환
     *
     * @param member Member 엔티티
     * @return MemberResponse
     */
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getUserId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                member.getStatus(),
                member.getLastLoginAt(),
                member.getCreatedAt()
        );
    }
}
