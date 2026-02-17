package com.team.jpquiz.member.query.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.member.command.domain.Member;
import com.team.jpquiz.member.command.infrastructure.MemberRepository;
import com.team.jpquiz.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 Query 서비스
 * 회원 정보 조회 등 읽기 전용 작업 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    /**
     * 내 정보 조회
     *
     * @param userId 회원 ID
     * @return 회원 정보 응답 DTO
     */
    public MemberResponse getMyInfo(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }

    /**
     * 회원 ID로 조회
     *
     * @param userId 회원 ID
     * @return 회원 정보 응답 DTO
     */
    public MemberResponse findById(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }
}