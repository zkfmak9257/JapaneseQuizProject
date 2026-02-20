package com.team.jpquiz.stats.query.infrastructure;

import com.team.jpquiz.stats.dto.response.StatsResponse;
<<<<<<< Updated upstream
=======
import java.util.List;
>>>>>>> Stashed changes
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatsMapper {

    StatsResponse.MyStats findMyStats(@Param("memberId") Long memberId);

    StatsResponse.AdminOverview findAdminOverview();
<<<<<<< Updated upstream
=======

    List<StatsResponse.CategoryAccuracy> findCategoryAccuracyByLatest();

    List<StatsResponse.CategoryAccuracy> findCategoryAccuracyByFirst();

    List<StatsResponse.QuestionStats> findQuestionStatsByLatest(
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countQuestionStatsByLatest();

    List<StatsResponse.QuestionStats> findQuestionStatsByFirst(
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countQuestionStatsByFirst();

    List<StatsResponse.TopWrongQuestion> findTopWrongQuestions(@Param("limit") int limit);

    List<StatsResponse.LearningRanking> findLearningRanking(@Param("limit") int limit);
>>>>>>> Stashed changes
}
