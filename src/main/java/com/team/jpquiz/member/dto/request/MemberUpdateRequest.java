package com.team.jpquiz.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정 요청 DTO
 * null인 필드는 수정하지 않음 (부분 수정 지원)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 정보 수정 요청")
public class MemberUpdateRequest {

    @Schema(description = "닉네임 (2~50자)", example = "새닉네임")
    @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하여야 합니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9_]+$",
            message = "닉네임은 한글, 영문, 숫자, 언더스코어(_)만 사용 가능합니다."
    )
    private String nickname;

    @Schema(description = "현재 비밀번호 (비밀번호 변경 시 필수)", example = "CurrentPassword123!")
    private String currentPassword;

    @Schema(description = "새 비밀번호 (8~50자, 영문/숫자/특수문자 포함)", example = "NewPassword123!")
    @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."
    )
    private String newPassword;

    /**
     * 닉네임 변경 요청인지 확인
     */
    public boolean hasNicknameChange() {
        return nickname != null && !nickname.isBlank();
    }

    /**
     * 비밀번호 변경 요청인지 확인
     */
    public boolean hasPasswordChange() {
        return newPassword != null && !newPassword.isBlank();
    }
}