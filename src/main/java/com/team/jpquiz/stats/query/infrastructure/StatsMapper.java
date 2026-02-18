package com.team.jpquiz.stats.query.infrastructure;

import com.team.jpquiz.stats.dto.response.StatsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatsMapper {

    StatsResponse.MyStats findMyStats(@Param("memberId") Long memberId);

    StatsResponse.AdminOverview findAdminOverview();
}
