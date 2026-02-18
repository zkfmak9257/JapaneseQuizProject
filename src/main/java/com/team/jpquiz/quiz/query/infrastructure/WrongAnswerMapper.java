package com.team.jpquiz.quiz.query.infrastructure;

import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
// 오답노트 조회 전용 SQL을 실행하는 MyBatis 매퍼입니다.
public interface WrongAnswerMapper {

  // 오답 목록 조회(페이징)
  List<WrongAnswerResponse> findWrongAnswers(
      @Param("memberId") Long memberId,
      @Param("offset") int offset,
      @Param("limit") int limit,
      @Param("fromDateTime") LocalDateTime fromDateTime
  );

  // 회원별 오답노트 전체 건수를 조회합니다.
  long countWrongAnswers(
      @Param("memberId") Long memberId,
      @Param("fromDateTime") LocalDateTime fromDateTime
  );
}
