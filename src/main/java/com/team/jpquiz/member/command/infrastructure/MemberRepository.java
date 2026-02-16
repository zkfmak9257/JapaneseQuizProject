package com.team.jpquiz.member.command.infrastructure;

import com.team.jpquiz.member.command.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일 중복 확인
     *
     * @param email 이메일
     * @return 존재하면 true
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임 중복 확인
     *
     * @param nickname 닉네임
     * @return 존재하면 true
     */
    boolean existsByNickname(String nickname);

    /**
     * 이메일로 회원 조회
     *
     * @param email 이메일
     * @return 회원 Optional
     */
    Optional<Member> findByEmail(String email);

    /**
     * 닉네임으로 회원 조회
     *
     * @param nickname 닉네임
     * @return 회원 Optional
     */
    Optional<Member> findByNickname(String nickname);

    /**
     * 닉네임 중복 확인 (본인 제외)
     *
     * @param nickname 닉네임
     * @param userId   본인 ID
     * @return 존재하면 true
     */
    boolean existsByNicknameAndUserIdNot(String nickname, Long userId);
}
