package com.team.jpquiz.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.jpquiz.common.dto.ErrorResponse;
import com.team.jpquiz.common.security.JwtTokenProvider;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.member.command.infrastructure.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity // @PreAuthorize같은 메서드 단위 인가 어노테이션 쓸 수 있게 해줌
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final List<String> allowedOrigins;
    private final List<String> allowedMethods;
    private final List<String> allowedHeaders;
    private final List<String> exposedHeaders;
    private final boolean allowCredentials;
    private final long maxAge;

    public SecurityConfig(
            ObjectMapper objectMapper,
            @Value("${app.cors.allowed-origins:}") String allowedOrigins,
            @Value("${app.cors.allowed-methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}") String allowedMethods,
            @Value("${app.cors.allowed-headers:*}") String allowedHeaders,
            @Value("${app.cors.exposed-headers:Authorization,Content-Type,X-Total-Count}") String exposedHeaders,
            @Value("${app.cors.allow-credentials:true}") boolean allowCredentials,
            @Value("${app.cors.max-age:3600}") long maxAge) {
        this.objectMapper = objectMapper;
        this.allowedOrigins = splitCsv(allowedOrigins);
        this.allowedMethods = splitCsv(allowedMethods);
        this.allowedHeaders = splitCsv(allowedHeaders);
        this.exposedHeaders = splitCsv(exposedHeaders);
        this.allowCredentials = allowCredentials;
        this.maxAge = maxAge;
    }

    private List<String> splitCsv(String value) {
        if (value == null || value.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
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
                        // Swagger, Actuator
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/favicon.ico").permitAll()

                        // 인증 API - 공개
                        .requestMatchers("/api/auth/**").permitAll()

                        // 퀴즈 API - 비회원도 접근 가능 (횟수 제한은 서비스 레이어에서 처리)
                        .requestMatchers(HttpMethod.GET, "/api/quizzes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/quizzes/*/submit").permitAll()

                        // 통계 API - 로그인 필수
                        .requestMatchers("/api/stats/**").authenticated()

                        // 랭킹 API - 로그인 필수
                        .requestMatchers("/api/rankings/**").authenticated()

                        // 회원 API - 로그인 필수
                        .requestMatchers("/api/members/**").authenticated()

                        // 그 외 모든 요청 - 로그인 필수
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
     * application.yml의 app.cors.* 값을 사용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // yml에서 주입받은 값 사용
        if (!allowedOrigins.isEmpty()) {
            configuration.setAllowedOriginPatterns(allowedOrigins);
        }

        if (!allowedMethods.isEmpty()) {
            configuration.setAllowedMethods(allowedMethods);
        }

        if (!allowedHeaders.isEmpty()) {
            configuration.setAllowedHeaders(allowedHeaders);
        }

        if (!exposedHeaders.isEmpty()) {
            configuration.setExposedHeaders(exposedHeaders);
        }

        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

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

