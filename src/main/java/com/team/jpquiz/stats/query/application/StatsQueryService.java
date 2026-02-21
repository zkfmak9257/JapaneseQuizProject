package com.team.jpquiz.stats.query.application;

import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.stats.dto.response.StatsResponse;
import com.team.jpquiz.stats.query.infrastructure.StatsMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsQueryService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_TOP_LIMIT = 100;

    private final StatsMapper statsMapper;

    public StatsResponse.MyStats getMyStats(Long memberId) {
        validateMemberId(memberId);

        StatsResponse.MyStats raw = statsMapper.findMyStats(memberId);

        int totalAttempts = valueOrZero(raw != null ? raw.getTotalAttempts() : 0);
        int completedAttempts = valueOrZero(raw != null ? raw.getCompletedAttempts() : 0);
        int totalAnswers = valueOrZero(raw != null ? raw.getTotalAnswers() : 0);
        int correctAnswers = valueOrZero(raw != null ? raw.getCorrectAnswers() : 0);
        int recent7dAnswers = valueOrZero(raw != null ? raw.getRecent7dAnswers() : 0);

        return StatsResponse.MyStats.builder()
                .memberId(memberId)
                .totalAttempts(totalAttempts)
                .completedAttempts(completedAttempts)
                .totalAnswers(totalAnswers)
                .correctAnswers(correctAnswers)
                .recent7dAnswers(recent7dAnswers)
                .completionRate(calculateRate(completedAttempts, totalAttempts))
                .accuracyRate(calculateRate(correctAnswers, totalAnswers))
                .build();
    }

    public StatsResponse.AdminOverview getAdminOverview() {
        StatsResponse.AdminOverview raw = statsMapper.findAdminOverview();

        int totalAttempts = valueOrZero(raw != null ? raw.getTotalAttempts() : 0);
        int completedAttempts = valueOrZero(raw != null ? raw.getCompletedAttempts() : 0);
        int totalAnswers = valueOrZero(raw != null ? raw.getTotalAnswers() : 0);
        int correctAnswers = valueOrZero(raw != null ? raw.getCorrectAnswers() : 0);
        int activeUsers7d = valueOrZero(raw != null ? raw.getActiveUsers7d() : 0);

        return StatsResponse.AdminOverview.builder()
                .totalAttempts(totalAttempts)
                .completedAttempts(completedAttempts)
                .totalAnswers(totalAnswers)
                .correctAnswers(correctAnswers)
                .activeUsers7d(activeUsers7d)
                .completionRate(calculateRate(completedAttempts, totalAttempts))
                .accuracyRate(calculateRate(correctAnswers, totalAnswers))
                .build();
    }

    public List<StatsResponse.CategoryAccuracy> findCategoryAccuracy(String basis) {
        StatsBasis normalizedBasis = normalizeBasis(basis, StatsBasis.LATEST);
        List<StatsResponse.CategoryAccuracy> raw = normalizedBasis == StatsBasis.FIRST
                ? statsMapper.findCategoryAccuracyByFirst()
                : statsMapper.findCategoryAccuracyByLatest();

        List<StatsResponse.CategoryAccuracy> result = new ArrayList<>();
        for (StatsResponse.CategoryAccuracy item : raw) {
            int totalAnswers = valueOrZero(item.getTotalAnswers());
            int correctAnswers = valueOrZero(item.getCorrectAnswers());

            result.add(StatsResponse.CategoryAccuracy.builder()
                    .category(item.getCategory())
                    .totalAnswers(totalAnswers)
                    .correctAnswers(correctAnswers)
                    .accuracyRate(calculateRate(correctAnswers, totalAnswers))
                    .build());
        }
        return result;
    }

    public PageResponse<StatsResponse.QuestionStats> findQuestionStats(
            String basis,
            int page,
            int size
    ) {
        validatePage(page, size);
        int offset = (page - 1) * size;
        StatsBasis normalizedBasis = normalizeBasis(basis, StatsBasis.FIRST);

        List<StatsResponse.QuestionStats> raw;
        long totalElements;
        if (normalizedBasis == StatsBasis.FIRST) {
            raw = statsMapper.findQuestionStatsByFirst(offset, size);
            totalElements = statsMapper.countQuestionStatsByFirst();
        } else {
            raw = statsMapper.findQuestionStatsByLatest(offset, size);
            totalElements = statsMapper.countQuestionStatsByLatest();
        }

        List<StatsResponse.QuestionStats> content = new ArrayList<>();
        for (StatsResponse.QuestionStats item : raw) {
            int totalAnswers = valueOrZero(item.getTotalAnswers());
            int correctAnswers = valueOrZero(item.getCorrectAnswers());
            int wrongAnswers = valueOrZero(item.getWrongAnswers());
            double accuracyRate = calculateRate(correctAnswers, totalAnswers);
            double difficultyScore = round2(100.0 - accuracyRate);

            content.add(StatsResponse.QuestionStats.builder()
                    .questionId(item.getQuestionId())
                    .questionText(item.getQuestionText())
                    .category(item.getCategory())
                    .totalAnswers(totalAnswers)
                    .correctAnswers(correctAnswers)
                    .wrongAnswers(wrongAnswers)
                    .accuracyRate(accuracyRate)
                    .difficultyScore(difficultyScore)
                    .difficultyLevel(toDifficultyLevel(accuracyRate))
                    .build());
        }
        return PageResponse.of(content, page, size, totalElements);
    }

    public List<StatsResponse.TopWrongQuestion> findTopWrongQuestions(int limit, String basis) {
        validateLimit(limit);
        StatsBasis normalizedBasis = normalizeBasis(basis, StatsBasis.LATEST);

        List<StatsResponse.TopWrongQuestion> raw = normalizedBasis == StatsBasis.FIRST
                ? statsMapper.findTopWrongQuestionsByFirst(limit)
                : statsMapper.findTopWrongQuestionsByLatest(limit);
        List<StatsResponse.TopWrongQuestion> result = new ArrayList<>();
        for (StatsResponse.TopWrongQuestion item : raw) {
            int totalAnswers = valueOrZero(item.getTotalAnswers());
            int wrongAnswers = valueOrZero(item.getWrongAnswers());

            result.add(StatsResponse.TopWrongQuestion.builder()
                    .questionId(item.getQuestionId())
                    .questionText(item.getQuestionText())
                    .category(item.getCategory())
                    .totalAnswers(totalAnswers)
                    .wrongAnswers(wrongAnswers)
                    .wrongRate(calculateRate(wrongAnswers, totalAnswers))
                    .build());
        }
        return result;
    }

    private void validateMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void validatePage(int page, int size) {
        if (page < 1 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateLimit(int limit) {
        if (limit < 1 || limit > MAX_TOP_LIMIT) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private StatsBasis normalizeBasis(String basis, StatsBasis defaultBasis) {
        if (basis == null || basis.isBlank()) {
            return defaultBasis;
        }
        try {
            return StatsBasis.valueOf(basis.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private double calculateRate(int numerator, int denominator) {
        if (denominator <= 0) {
            return 0.0;
        }
        return round2((numerator * 100.0) / denominator);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String toDifficultyLevel(double accuracyRate) {
        if (accuracyRate < 40.0) {
            return "HARD";
        }
        if (accuracyRate < 70.0) {
            return "NORMAL";
        }
        return "EASY";
    }

    private enum StatsBasis {
        FIRST,
        LATEST
    }
}
