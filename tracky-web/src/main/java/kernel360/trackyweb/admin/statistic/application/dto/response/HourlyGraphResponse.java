package kernel360.trackyweb.admin.statistic.application.dto.response;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public record HourlyGraphResponse(
        int hour,
        long driveCount
) {
    public static List<HourlyGraphResponse> toResultList(List<HourlyGraphResponse> latestData) {
        Map<Integer, Long> hourToCount = latestData.stream()
                .collect(Collectors.toMap(HourlyGraphResponse::hour, HourlyGraphResponse::driveCount));

        List<HourlyGraphResponse> fullList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            long count = hourToCount.getOrDefault(i, 0L);
            fullList.add(new HourlyGraphResponse(i, count));
        }
        return fullList;
    }
}