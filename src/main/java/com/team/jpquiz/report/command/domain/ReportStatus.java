package com.team.jpquiz.report.command.domain;

public enum ReportStatus {
    PENDING,
    IN_REVIEW,
    RESOLVED,
    REJECTED;

    public boolean canTransitionTo(ReportStatus nextStatus) {
        if (nextStatus == null) {
            return false;
        }

        return switch (this) {
            case PENDING -> nextStatus == PENDING
                    || nextStatus == IN_REVIEW
                    || nextStatus == RESOLVED
                    || nextStatus == REJECTED;
            case IN_REVIEW -> nextStatus == IN_REVIEW
                    || nextStatus == RESOLVED
                    || nextStatus == REJECTED;
            case RESOLVED -> nextStatus == RESOLVED;
            case REJECTED -> nextStatus == REJECTED;
        };
    }
}
