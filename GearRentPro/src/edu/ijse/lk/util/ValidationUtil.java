package edu.ijse.lk.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class ValidationUtil {
    private ValidationUtil() {}

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateDateRange(LocalDate start, LocalDate end) {
        require(start != null && end != null, "Start and end dates are required.");
        require(!end.isBefore(start), "End date cannot be before start date.");
        require(ChronoUnit.DAYS.between(start, end) + 1 <= 30, "Maximum duration is 30 days.");
    }
}
