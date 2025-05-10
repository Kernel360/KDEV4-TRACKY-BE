package kernel360.trackyweb.admin.statistic.application.dto.response;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record HourlyGraphResponse(
        int hour,
        int driveCount
) {
    public static List<HourlyGraphResponse> toResultList(List<HourlyGraphResponse> latestData) {
        Map<Integer, Integer> hourToCount = latestData.stream()
                .collect(Collectors.toMap(HourlyGraphResponse::hour, HourlyGraphResponse::driveCount));

        List<HourlyGraphResponse> fullList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            int count = hourToCount.getOrDefault(i, 0);
            fullList.add(new HourlyGraphResponse(i, count));
        }

        return fullList;
    }
}