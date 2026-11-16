package com.example.onboarding.dto;

import java.time.LocalDate;

/** What the onboarding-status endpoint returns. */
public record OnboardingStatus(
        Long employeeId,
        String name,
        LocalDate onboardingDate,
        String onboardingDateLabel,
        int daysUntilOnboarding,
        int workingDaysUntilOnboarding,
        String status) {
}
