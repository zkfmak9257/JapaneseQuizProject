package com.team.jpquiz.quiz.query.infrastructure;

import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WrongAnswerMapper {

  // 오답 목록 조회(페이징)
  List<WrongAnswerResponse> findWrongAnswers(
      @Param("memberId") Long memberId,
      @Param("offset") int offset,
      @Param("limit") int limit
  );
  long countWrongAnswers(@Param("memberId") Long memberId);
}
