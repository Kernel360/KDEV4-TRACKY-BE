package kernel360.trackyweb.timedistance.domain.provider;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.admin.statistic.application.dto.response.HourlyGraphResponse;
import org.springframework.stereotype.Component;

import com.querydsl.core.Tuple;

import kernel360.trackycore.core.domain.entity.QTimeDistanceEntity;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationDistance;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationSeconds;
import kernel360.trackyweb.timedistance.infrastructure.repository.TimeDistanceDomainRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimeDistanceDomainProvider {

	private final TimeDistanceDomainRepository timeDistanceDomainRepository;

	public long[] getHourlyDriveCounts(Long bizId, LocalDate targetDate) {

		List<Tuple> hourlyDriveCount = timeDistanceDomainRepository.countByBizIdAndDateGroupedByHour(bizId, targetDate);

		long[] hourlyStat = new long[24];

		for (Tuple tuple : hourlyDriveCount) {
			Integer hour = tuple.get(QTimeDistanceEntity.timeDistanceEntity.hour);
			Long driveCount = tuple.get(QTimeDistanceEntity.timeDistanceEntity.count());

			if (hour != null && driveCount != null) {
				hourlyStat[hour] = driveCount;
			}
		}
		return hourlyStat;
	}

	public Map<Long, Long> calculateDailyOperationTime(LocalDate targetDate) {
		return OperationSeconds.toMap(timeDistanceDomainRepository.getDailyOperationTime(targetDate));
	}

	public Map<Long, Double> calculateDailyOperationDistance(LocalDate targetDate) {
		return OperationDistance.toMap(timeDistanceDomainRepository.getDailyOperationDistance(targetDate));
	}

	public List<HourlyGraphResponse> getYesterdayData() {
		return timeDistanceDomainRepository.getYesterdayData();
	}
}
