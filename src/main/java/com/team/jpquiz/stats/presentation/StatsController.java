package com.team.jpquiz.stats.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.common.util.SecurityUtil;
import com.team.jpquiz.stats.dto.response.StatsResponse;
import com.team.jpquiz.stats.query.application.StatsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Stats", description = "통계 API")
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
    @Operation(tags = {"Admin"})
    public ApiResponse<StatsResponse.AdminOverview> getAdminOverview() {
        StatsResponse.AdminOverview response = statsQueryService.getAdminOverview();
        return ApiResponse.ok(response);
    }

    @GetMapping("/admin/stats/categories")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(tags = {"Admin"})
    public ApiResponse<List<StatsResponse.CategoryAccuracy>> getCategoryAccuracy(
            @RequestParam(defaultValue = "latest") String basis
    ) {
        List<StatsResponse.CategoryAccuracy> response = statsQueryService.findCategoryAccuracy(basis);
        return ApiResponse.ok(response);
    }

    @GetMapping("/admin/stats/questions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(tags = {"Admin"})
    public ApiResponse<PageResponse<StatsResponse.QuestionStats>> getQuestionStats(
            @RequestParam(defaultValue = "first") String basis,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<StatsResponse.QuestionStats> response =
                statsQueryService.findQuestionStats(basis, page, size);
        return ApiResponse.ok(response);
    }

    @GetMapping("/admin/stats/questions/top-wrong")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(tags = {"Admin"})
    public ApiResponse<List<StatsResponse.TopWrongQuestion>> getTopWrongQuestions(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "latest") String basis
    ) {
        List<StatsResponse.TopWrongQuestion> response = statsQueryService.findTopWrongQuestions(limit, basis);
        return ApiResponse.ok(response);
    }

}
