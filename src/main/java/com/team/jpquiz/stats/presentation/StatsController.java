package com.team.jpquiz.stats.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.stats.dto.response.StatsResponse;
import com.team.jpquiz.stats.query.application.StatsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StatsController {

    private final StatsQueryService statsQueryService;

    @GetMapping("/stats/me")
    public ApiResponse<StatsResponse.MyStats> getMyStats() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        StatsResponse.MyStats response = statsQueryService.getMyStats(memberId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/admin/stats/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<StatsResponse.AdminOverview> getAdminOverview() {
        StatsResponse.AdminOverview response = statsQueryService.getAdminOverview();
        return ApiResponse.ok(response);
    }
}
