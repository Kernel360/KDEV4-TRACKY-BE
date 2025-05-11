package kernel360.trackyweb.statistic.domain.provider;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import kernel360.trackyweb.admin.statistic.application.dto.response.GraphsResponse;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.MonthlyStatisticEntity;
import kernel360.trackyweb.statistic.application.dto.response.MonthlyStatisticResponse;
import kernel360.trackyweb.statistic.infrastructure.repository.monthly.MonthlyStatisticDomainRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonthlyStatisticProvider {

	private final MonthlyStatisticDomainRepository monthlyStatisticRepository;

	public MonthlyStatisticEntity getMonthlyStatistic(Long bizId, LocalDate date) {

		return monthlyStatisticRepository.findByBizIdAndDate(bizId, date)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.STATISTIC_NOT_FOUND));
	}

	@Transactional
	public void saveMonthlyStatistic(List<MonthlyStatisticEntity> resultEntities) {

		for (MonthlyStatisticEntity entity : resultEntities) {
			Optional<MonthlyStatisticEntity> existEntity = monthlyStatisticRepository.findByBizIdAndDate(
				entity.getBizId(), entity.getDate().minusDays(1));

			if (existEntity.isPresent()) {
				MonthlyStatisticEntity existing = existEntity.get();
				existing.update(entity.getTotalCarCount(), entity.getNonOperatingCarCount(),
					entity.getAvgOperationRate(), entity.getTotalDriveSec(), entity.getTotalDriveCount(),
					entity.getTotalDriveDistance());
			} else {
				monthlyStatisticRepository.save(entity);
			}
		}
	}

	public List<MonthlyStatisticResponse.MonthlyStats> getMonthlyStats(Long bizId, LocalDate currentDate,
		LocalDate targetDate) {

		return monthlyStatisticRepository.getMonthlyStats(bizId, currentDate, targetDate);
	}

	public MonthlyStatisticEntity getDashBoardStatistic(String bizUuid) {
		return monthlyStatisticRepository.findLatestMonthlyStatistic(bizUuid);
	}

	public List<GraphsResponse.NonOperatedCar> getNonOperatedCarWithBizName() {
		return monthlyStatisticRepository.getNonOperatedCarWithBizName();
	}

	public List<GraphsResponse.DriveCount> getDriveCount() {
		return monthlyStatisticRepository.getTotalDriveCount();
	}
}
