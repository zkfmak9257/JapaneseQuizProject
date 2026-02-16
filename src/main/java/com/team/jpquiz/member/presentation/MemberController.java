package com.team.jpquiz.member.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.global.security.UserPrincipal;
import com.team.jpquiz.member.command.application.MemberCommandService;
import com.team.jpquiz.member.dto.request.MemberUpdateRequest;
import com.team.jpquiz.member.dto.request.WithdrawRequest;
import com.team.jpquiz.member.dto.response.MemberResponse;
import com.team.jpquiz.member.query.application.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 API 컨트롤러
 * 회원 정보 조회, 수정, 탈퇴 등
 */
@Tag(name = "Member", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;


    /**
     * 내 정보 조회 API
     *
     * @param userPrincipal 인증된 사용자 정보
     * @return 회원 정보 응답
     */
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 필요"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음"
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MemberResponse memberResponse = memberQueryService.getMyInfo(userPrincipal.getUserId());
        return ResponseEntity
                .ok(ApiResponse.ok("회원 정보를 조회했습니다.", memberResponse));
    }

    /**
     * 회원 정보 수정 API
     *
     * @param userPrincipal 인증된 사용자 정보
     * @param request       회원 정보 수정 요청 DTO
     * @return 성공 응답
     */
    @Operation(summary = "회원 정보 수정", description = "닉네임 또는 비밀번호를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 수정 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효성 검사 실패 또는 현재 비밀번호 미입력)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (현재 비밀번호 불일치)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "닉네임 중복"
            )
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateMember(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        memberCommandService.updateMember(userPrincipal.getUserId(), request);
        return ResponseEntity
                .ok(ApiResponse.ok("회원 정보가 수정되었습니다.", null));
    }

    /**
     * 회원 탈퇴 API
     *
     * @param userPrincipal 인증된 사용자 정보
     * @param request       탈퇴 요청 DTO (비밀번호 확인)
     * @return 성공 응답
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 처리합니다. 비밀번호 확인이 필요합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원 탈퇴 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (이미 탈퇴한 회원)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (비밀번호 불일치)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음"
            )
    })
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody WithdrawRequest request
    ) {
        memberCommandService.withdraw(userPrincipal.getUserId(), request);
        return ResponseEntity
                .ok(ApiResponse.ok("회원 탈퇴가 완료되었습니다.", null));
    }
}