package com.team.jpquiz.auth.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.member.command.application.MemberCommandService;
import com.team.jpquiz.member.dto.request.MemberLoginRequest;
import com.team.jpquiz.member.dto.request.MemberRegisterRequest;
import com.team.jpquiz.member.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API 컨트롤러
 * 회원가입, 로그인, 토큰 갱신 등
 */
@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberCommandService memberCommandService;

    /**
     * 회원가입 API
     *
     * @param request 회원가입 요청 DTO
     * @return JWT 토큰 응답
     */
    @Operation(summary = "회원가입", description = "새로운 회원을 등록하고 JWT 토큰을 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효성 검사 실패)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이메일 또는 닉네임 중복"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TokenResponse>> register(
            @Valid @RequestBody MemberRegisterRequest request
    ) {
        TokenResponse tokenResponse = memberCommandService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("회원가입이 완료되었습니다.", tokenResponse));
    }

    /**
     * 로그인 API
     *
     * @param request 로그인 요청 DTO
     * @return JWT 토큰 응답
     */
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (이메일 또는 비밀번호 불일치)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "비활성화된 계정"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody MemberLoginRequest request
    ) {
        TokenResponse tokenResponse = memberCommandService.login(request);
        return ResponseEntity
                .ok(ApiResponse.ok("로그인이 완료되었습니다.", tokenResponse));
    }
}