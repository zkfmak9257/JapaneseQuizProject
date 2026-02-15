package com.team.jpquiz.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.jpquiz.common.dto.ErrorResponse;
import com.team.jpquiz.common.security.JwtTokenProvider;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.member.command.infrastructure.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity // @PreAuthorize같은 메서드 단위 인가 어노테이션 쓸 수 있게 해줌
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {

        // API 서버 기준이므로 웹 폼 로그인 / 기본 인증을 끄고, CSRF 비활성화 (기본 접근 정책의 출발선)
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 인증 정보는 매 요청마다 토큰에서 판별하도록 하는 전제
                // JWT 기반 API는 서버 세션을 사용하지 않기 때문에 STATELESS로 고정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 접근 정책 공개 엔드포인트는 통과 그 외는 인증 필요로 묶어두는 정책
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/quiz/attempts/*/questions/*").permitAll()
                        .anyRequest().authenticated()

                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                        .accessDeniedHandler(jwtAccessDeniedHandler())
                )
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }

    /**
     * JWT 인증 필터 Bean 등록
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            MemberRepository memberRepository
    ) {
        return new JwtAuthenticationFilter(jwtTokenProvider, memberRepository);
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder Bean
     * BCrypt 해시 알고리즘 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 설정
     * 프론트엔드에서 API 호출 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 Origin (프론트엔드 주소)
        // TODO: 운영 환경에서는 실제 도메인으로 변경 필요
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",    // React 개발 서버
                "http://localhost:5173",    // Vite 개발 서버
                "http://localhost:8080"     // 로컬 테스트
        ));

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // 브라우저에 노출할 헤더 (Authorization 헤더 등)
        configuration.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Total-Count"
        ));

        // 인증 정보 포함 여부 (쿠키, Authorization 헤더 등)
        configuration.setAllowCredentials(true);

        // Preflight 요청 캐시 시간 (초)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 적용

        return source;
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return (request, response, authException) -> writeErrorResponse(
                response,
                request,
                ErrorCode.UNAUTHORIZED
        );

    }

    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> writeErrorResponse(
                response,
                request,
                ErrorCode.FORBIDDEN
        );
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            HttpServletRequest request,
            ErrorCode errorCode
    ) throws IOException {
        ErrorResponse body = ErrorResponse.of(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value(),
                request.getRequestURI()
        );

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
