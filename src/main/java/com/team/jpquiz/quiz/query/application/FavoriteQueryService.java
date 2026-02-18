package com.team.jpquiz.quiz.query.application;

import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.quiz.dto.response.FavoriteResponse;
import com.team.jpquiz.quiz.query.infrastructure.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteQueryService {

    private final FavoriteMapper favoriteMapper;

    public PageResponse<FavoriteResponse> getFavoriteList(Long memberId, int page, int size, String category) {
        validateInput(memberId, page, size);
        int offset = (page - 1) * size;

        List<FavoriteResponse> content = favoriteMapper.findFavorites(memberId, offset, size, category);
        long totalElements = favoriteMapper.countFavorites(memberId, category);
        return PageResponse.of(content, page, size, totalElements);
    }

    private void validateInput(Long memberId, int page, int size) {
        if (memberId == null || memberId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (page < 1 || size < 1 || size > 100) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}
