package com.team.jpquiz.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.jpquiz.common.dto.ErrorResponse;
import com.team.jpquiz.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
@EnableMethodSecurity // @PreAuthorize같은 메서드 단위 인가 어노테이션 쓸 수 있게 해줌
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // API 서버 기준이므로 웹 폼 로그인 / 기본 인증을 끄고, CSRF 비활성화 (기본 접근 정책의 출발선)
        http
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
                );


        return http.build();

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

