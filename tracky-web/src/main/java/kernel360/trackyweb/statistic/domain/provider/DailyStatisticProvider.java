package kernel360.trackyweb.statistic.domain.provider;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import kernel360.trackyweb.admin.statistic.application.dto.response.GraphsResponse;
import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.DailyStatisticEntity;
import kernel360.trackyweb.statistic.application.dto.internal.OperationCount;
import kernel360.trackyweb.statistic.application.dto.internal.OperationDistance;
import kernel360.trackyweb.statistic.application.dto.internal.OperationRate;
import kernel360.trackyweb.statistic.application.dto.internal.OperationTime;
import kernel360.trackyweb.statistic.application.dto.internal.TotalCarCount;
import kernel360.trackyweb.statistic.infrastructure.repository.daily.DailyStatisticDomainRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DailyStatisticProvider {

	private final DailyStatisticDomainRepository dailyStatisticDomainRepository;

	//일일 통계 테이블 저장
	public void saveDailyStatistic(List<DailyStatisticEntity> resultEntities) {
		dailyStatisticDomainRepository.saveAll(resultEntities);
	}

	public DailyStatisticEntity getDailyStatistic(Long bizId, LocalDate date) {

		return dailyStatisticDomainRepository.findByBizIdAndDate(bizId, date)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.STATISTIC_NOT_FOUND));
	}

	//(월별) 말일의 total car count
	public Map<Long, Integer> getLastTotalCarCount(LocalDate targetDate) {
		return TotalCarCount.toMap(dailyStatisticDomainRepository.getLastTotalCarCount(targetDate));
	}

	//(월별) 평균 운행량
	public Map<Long, Double> getOperationRatesAvg(LocalDate targetDate) {
		return OperationRate.toMap(dailyStatisticDomainRepository.findAverageOperationRate(targetDate));
	}

	//(월별) 총 운행 횟수
	public Map<Long, Integer> getOperationCountsTotal(LocalDate targetDate) {
		return OperationCount.toMap(dailyStatisticDomainRepository.findSumOperationCount(targetDate));
	}

	//(월별) 총 운행 시간
	public Map<Long, Long> getOperationTimesTotal(LocalDate targetDate) {
		return OperationTime.toMap(dailyStatisticDomainRepository.findSumOperationTime(targetDate));
	}

	public Map<Long, Double> getOperationDistancesTotal(LocalDate targetDate) {
		return OperationDistance.toMap(dailyStatisticDomainRepository.findSumOperationDistance(targetDate));
	}

	public List<Integer> getDailyDriveCount(String bizUuid) {
		return dailyStatisticDomainRepository.findDriveCountByBizUuid(bizUuid);
	}

	public List<GraphsResponse.CarCount> getCarCountWithBizName() {
		return dailyStatisticDomainRepository.getCarCountAndBizName();
	}

	public List<GraphsResponse.OperationRate> getOperationRatesAvgWithBizName() {
		return dailyStatisticDomainRepository.getOperationRateAndBizName();
	}
}
