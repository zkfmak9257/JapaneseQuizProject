package com.team.jpquiz.quiz.query.infrastructure;

import com.team.jpquiz.quiz.dto.response.FavoriteResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

  List<FavoriteResponse> findFavorites(
      @Param("memberId") Long memberId,
      @Param("offset") int offset,
      @Param("limit") int limit,
      @Param("category") String category
  );

  long countFavorites(
      @Param("memberId") Long memberId,
      @Param("category") String category
  );
}
