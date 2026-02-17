package com.team.jpquiz.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 탈퇴 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 탈퇴 요청")
public class WithdrawRequest {

    @Schema(description = "현재 비밀번호 (본인 확인용)", example = "Password123!")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
