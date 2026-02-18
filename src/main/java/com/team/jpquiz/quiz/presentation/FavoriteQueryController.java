package com.team.jpquiz.quiz.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.global.security.UserPrincipal;
import com.team.jpquiz.quiz.dto.response.FavoriteResponse;
import com.team.jpquiz.quiz.query.application.FavoriteQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteQueryController {

    private final FavoriteQueryService favoriteQueryService;

    @GetMapping
    public ApiResponse<PageResponse<FavoriteResponse>> getFavoriteList(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category
    ) {
        PageResponse<FavoriteResponse> response =
                favoriteQueryService.getFavoriteList(userPrincipal.getUserId(), page, size, category);
        return ApiResponse.ok(response);
    }
}
