package com.team.jpquiz.stats.query.application;

import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
import com.team.jpquiz.stats.dto.response.StatsResponse;
import com.team.jpquiz.stats.query.infrastructure.StatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsQueryService {

    private final StatsMapper statsMapper;

    public StatsResponse.MyStats getMyStats(Long memberId) {
        validateMemberId(memberId);

        StatsResponse.MyStats raw = statsMapper.findMyStats(memberId);

        int totalAttempts = valueOrZero(raw != null ? raw.getTotalAttempts() : null);
        int completedAttempts = valueOrZero(raw != null ? raw.getCompletedAttempts() : null);
        int totalAnswers = valueOrZero(raw != null ? raw.getTotalAnswers() : null);
        int correctAnswers = valueOrZero(raw != null ? raw.getCorrectAnswers() : null);
        int recent7dAnswers = valueOrZero(raw != null ? raw.getRecent7dAnswers() : null);

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

        int totalAttempts = valueOrZero(raw != null ? raw.getTotalAttempts() : null);
        int completedAttempts = valueOrZero(raw != null ? raw.getCompletedAttempts() : null);
        int totalAnswers = valueOrZero(raw != null ? raw.getTotalAnswers() : null);
        int correctAnswers = valueOrZero(raw != null ? raw.getCorrectAnswers() : null);
        int activeUsers7d = valueOrZero(raw != null ? raw.getActiveUsers7d() : null);

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

    private void validateMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private double calculateRate(int numerator, int denominator) {
        if (denominator <= 0) {
            return 0.0;
        }
        return Math.round((numerator * 10000.0) / denominator) / 100.0;
    }
}
