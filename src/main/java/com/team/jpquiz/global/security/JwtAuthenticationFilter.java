package com.team.jpquiz.global.security;

import com.team.jpquiz.common.security.JwtTokenProvider;
import com.team.jpquiz.member.command.domain.Member;
import com.team.jpquiz.member.command.infrastructure.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 요청마다 Authorization 헤더에서 JWT 토큰을 추출하여 검증하고,
 * 유효한 경우 SecurityContext에 인증 정보를 설정
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. 요청에서 JWT 토큰 추출
            String token = extractTokenFromRequest(request);

            // 2. 토큰이 존재하고 유효한 경우
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                // 3. Access Token인지 확인 (Refresh Token은 제외)
                if (!jwtTokenProvider.isAccessToken(token)) {
                    log.debug("Refresh token detected in Authorization header, skipping authentication");
                    filterChain.doFilter(request, response);
                    return;
                }

                // 4. 토큰에서 사용자 ID 추출
                Long userId = jwtTokenProvider.getUserIdFromToken(token);

                // 5. DB에서 사용자 조회
                Member member = memberRepository.findById(userId)
                        .orElse(null);

                // 6. 사용자가 존재하고 활성 상태인 경우에만 인증 정보 설정
                if (member != null && member.isActive()) {
                    // UserPrincipal 생성 (UserDetails 구현체)
                    UserPrincipal userPrincipal = UserPrincipal.from(member);

                    // Authentication 객체 생성
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userPrincipal,                     // principal
                                    null,                              // credentials (비밀번호 불필요)
                                    userPrincipal.getAuthorities()     // authorities (권한)
                            );

                    // 요청 정보 추가 (IP, Session ID 등)
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // SecurityContext에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("Set authentication for user: {} (userId: {})",
                            member.getEmail(), userId);
                }
            }
        } catch (Exception ex) {
            // 필터에서 발생한 예외는 로그만 남기고 넘어감
            // SecurityConfig의 authenticationEntryPoint가 401 처리
            log.error("Cannot set user authentication: {}", ex.getMessage());
        }

        // 7. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    /**
     * 특정 요청에 대해 필터를 실행하지 않도록 설정 (최적화)
     * 공개 엔드포인트는 필터 실행 생략
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 공개 엔드포인트는 필터 실행 안 함
        return path.startsWith("/api/auth/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/")
                || path.equals("/actuator/health")
                || path.equals("/")
                || path.equals("/favicon.ico");
    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     *
     * @param request HTTP 요청
     * @return JWT 토큰 (Bearer 접두사 제거), 없으면 null
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거 (7글자)
        }

        return null;
    }
}