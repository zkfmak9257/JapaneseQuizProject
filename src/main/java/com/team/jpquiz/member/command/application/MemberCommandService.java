package com.team.jpquiz.member.command.application;

import com.team.jpquiz.common.security.JwtTokenProvider;
import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.member.command.domain.Member;
import com.team.jpquiz.member.command.infrastructure.MemberRepository;
import com.team.jpquiz.member.dto.request.MemberLoginRequest;
import com.team.jpquiz.member.dto.request.MemberRegisterRequest;
import com.team.jpquiz.member.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 6. JWT 토큰 생성 및 반환
        String accessToken = jwtTokenProvider.generateAccessToken(savedMember);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedMember);

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

        // 5. JWT 토큰 생성 및 반환
        String accessToken = jwtTokenProvider.generateAccessToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        return TokenResponse.of(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationInSeconds()
        );
    }
}