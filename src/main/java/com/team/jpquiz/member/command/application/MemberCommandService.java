package com.team.jpquiz.member.command.application;

import com.team.jpquiz.common.security.JwtTokenProvider;
import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.member.command.domain.Member;
import com.team.jpquiz.member.command.domain.RefreshToken;
import com.team.jpquiz.member.command.infrastructure.MemberRepository;
import com.team.jpquiz.member.command.infrastructure.RefreshTokenRepository;
import com.team.jpquiz.member.dto.request.MemberLoginRequest;
import com.team.jpquiz.member.dto.request.MemberRegisterRequest;
import com.team.jpquiz.member.dto.request.MemberUpdateRequest;
import com.team.jpquiz.member.dto.request.RefreshTokenRequest;
import com.team.jpquiz.member.dto.request.WithdrawRequest;
import com.team.jpquiz.member.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * 회원 Command 서비스
 * 회원가입, 정보 수정, 탈퇴 등 상태 변경 작업 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     *
     * @param request 회원가입 요청 DTO
     * @return JWT 토큰 응답
     */
    public TokenResponse register(MemberRegisterRequest request) {
        // 1. 이메일 중복 검사
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 2. 닉네임 중복 검사
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. 회원 엔티티 생성
        Member member = Member.create(
                request.getEmail(),
                encodedPassword,
                request.getNickname()
        );

        // 5. 회원 저장
        Member savedMember = memberRepository.save(member);
        log.info("회원가입 완료: userId={}, email={}", savedMember.getUserId(), savedMember.getEmail());

        // 6. JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(savedMember);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedMember);

        // 7. Refresh Token을 DB에 저장 (폐기 가능하도록)
        saveRefreshToken(savedMember.getUserId(), refreshToken);

        return TokenResponse.of(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationInSeconds()
        );
    }

    /**
     * 로그인
     *
     * @param request 로그인 요청 DTO
     * @return JWT 토큰 응답
     */
    public TokenResponse login(MemberLoginRequest request) {
        // 1. 이메일로 회원 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. 계정 상태 확인 (활성 상태인지)
        if (!member.isActive()) {
            throw new CustomException(ErrorCode.INACTIVE_MEMBER);
        }

        // 4. 마지막 로그인 시각 업데이트
        member.updateLastLoginAt();
        log.info("로그인 성공: userId={}, email={}", member.getUserId(), member.getEmail());

        // 5. JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        // 6. Refresh Token을 DB에 저장 (폐기 가능하도록)
        saveRefreshToken(member.getUserId(), refreshToken);

        return TokenResponse.of(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationInSeconds()
        );
    }

    /**
     * 회원 정보 수정
     *
     * @param userId  회원 ID
     * @param request 회원 정보 수정 요청 DTO
     */
    public void updateMember(Long userId, MemberUpdateRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 닉네임 변경
        if (request.hasNicknameChange()) {
            // 닉네임 중복 검사 (본인 제외)
            if (memberRepository.existsByNicknameAndUserIdNot(request.getNickname(), userId)) {
                throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
            }
            member.updateNickname(request.getNickname());
            log.info("닉네임 변경 완료: userId={}, newNickname={}", userId, request.getNickname());
        }

        // 3. 비밀번호 변경
        if (request.hasPasswordChange()) {
            // 현재 비밀번호 필수 확인
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                throw new CustomException(ErrorCode.CURRENT_PASSWORD_REQUIRED);
            }

            // 현재 비밀번호 검증
            if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }

            // 새 비밀번호 암호화 및 저장
            String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
            member.updatePassword(encodedNewPassword);

            // 비밀번호 변경 시 모든 Refresh Token 폐기 (보안 강화)
            refreshTokenRepository.revokeAllByUserId(userId, LocalDateTime.now());
            log.info("비밀번호 변경 완료, 모든 세션 무효화: userId={}", userId);
        }
    }

    /**
     * 토큰 갱신
     *
     * @param request 리프레시 토큰 요청 DTO
     * @return 새로운 JWT 토큰 응답
     */
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // 1. 리프레시 토큰 유효성 검증 (JWT 서명 및 만료)
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. 리프레시 토큰 타입 확인
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 3. DB에서 Refresh Token 존재 여부 확인 (폐기된 토큰인지 검증)
        String tokenHash = hashToken(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByRefreshTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        // 4. 저장된 토큰의 유효성 확인
        if (!storedToken.isValid()) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 5. 토큰에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 6. 회원 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 7. 계정 상태 확인
        if (!member.isActive()) {
            // 비활성 회원의 토큰은 모두 폐기
            refreshTokenRepository.revokeAllByUserId(userId, LocalDateTime.now());
            throw new CustomException(ErrorCode.INACTIVE_MEMBER);
        }

        // 8. 기존 Refresh Token 폐기 후 새로 발급 (Refresh Token Rotation)
        storedToken.revoke();
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(userId, newRefreshToken);

        // 9. 새로운 Access Token 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(member);
        log.info("토큰 갱신 완료: userId={}", userId);

        return TokenResponse.of(
                newAccessToken,
                newRefreshToken,
                jwtTokenProvider.getAccessTokenExpirationInSeconds()
        );
    }

    /**
     * 회원 탈퇴
     *
     * @param userId  회원 ID
     * @param request 탈퇴 요청 DTO
     */
    public void withdraw(Long userId, WithdrawRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 이미 탈퇴한 회원인지 확인
        if (!member.isActive()) {
            throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
        }

        // 3. 비밀번호 검증 (본인 확인)
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 4. 모든 Refresh Token 폐기
        refreshTokenRepository.revokeAllByUserId(userId, LocalDateTime.now());

        // 5. 회원 탈퇴 처리
        member.withdraw();
        log.info("회원 탈퇴 완료: userId={}, email={}", userId, member.getEmail());
    }

    /**
     * Refresh Token을 해시하여 DB에 저장
     *
     * @param userId       사용자 ID
     * @param refreshToken Refresh Token 문자열
     */
    private void saveRefreshToken(Long userId, String refreshToken) {
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtTokenProvider.getRefreshTokenExpirationInMillis() / 1000);

        String tokenHash = hashToken(refreshToken);
        RefreshToken token = RefreshToken.create(userId, tokenHash, expiresAt);
        refreshTokenRepository.save(token);
    }

    /**
     * 토큰을 SHA-256으로 해시
     *
     * @param token 원본 토큰
     * @return Base64 인코딩된 해시값
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}