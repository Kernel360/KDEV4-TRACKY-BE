package kernel360.trackyweb.admin.statistic.application.dto.response;

import kernel360.trackycore.core.domain.entity.MonthlyStatisticEntity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MonthlyDriveCountResponse(
        LocalDate date,
        int driveCount
) {
    public static List<MonthlyDriveCountResponse> toDto(List<MonthlyStatisticEntity> monthlyStatisticEntities) {
        if (monthlyStatisticEntities == null || monthlyStatisticEntities.isEmpty()) {
            return Collections.emptyList();
        }
        YearMonth thisMonth = YearMonth.now();
        YearMonth twelveMonthsAgo = thisMonth.minusMonths(11);

        Map<YearMonth, Integer> monthToDriveCount = new HashMap<>();
        monthlyStatisticEntities.forEach(entity -> {
            YearMonth yearMonth = YearMonth.from(entity.getDate());
            monthToDriveCount.put(yearMonth, entity.getTotalDriveCount());
        });

        return IntStream.range(0, 12)
                .mapToObj(i -> {
                    YearMonth yearMonth = twelveMonthsAgo.plusMonths(i);
                    int count = monthToDriveCount.getOrDefault(yearMonth, 0);
                    return new MonthlyDriveCountResponse(yearMonth.atEndOfMonth(), count);
                })
                .collect(Collectors.toList());
    }
}

