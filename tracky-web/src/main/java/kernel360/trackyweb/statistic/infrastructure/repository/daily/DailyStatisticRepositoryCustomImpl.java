package kernel360.trackyweb.statistic.infrastructure.repository.daily;

import static kernel360.trackycore.core.domain.entity.QDailyStatisticEntity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackyweb.statistic.application.dto.internal.OperationCount;
import kernel360.trackyweb.statistic.application.dto.internal.OperationDistance;
import kernel360.trackyweb.statistic.application.dto.internal.OperationRate;
import kernel360.trackyweb.statistic.application.dto.internal.OperationTime;
import kernel360.trackyweb.statistic.application.dto.internal.TotalCarCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DailyStatisticRepositoryCustomImpl implements DailyStatisticRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	//말일의 total car count 값
	@Override
	public List<TotalCarCount> getLastTotalCarCount(LocalDate targetDate) {
		return queryFactory
			.select(Projections.constructor(
				TotalCarCount.class,
				dailyStatisticEntity.bizId,
				dailyStatisticEntity.totalCarCount
			))
			.from(dailyStatisticEntity)
			.orderBy(dailyStatisticEntity.date.desc())
			.groupBy(dailyStatisticEntity.bizId)
			.fetch();
	}

	//차량 가동률의 평균
	@Override
	public List<OperationRate> findAverageOperationRate(LocalDate targetDate) {
		LocalDate firstDay = targetDate.withDayOfMonth(1);

		return queryFactory
			.select(Projections.constructor(
				OperationRate.class,
				dailyStatisticEntity.bizId,
				dailyStatisticEntity.avgOperationRate.avg()
			))
			.from(dailyStatisticEntity)
			.where(dailyStatisticEntity.date.between(firstDay, targetDate))
			.groupBy(dailyStatisticEntity.bizId)
			.fetch();
	}

	//차량 운행 횟수의 총 합계
	@Override
	public List<OperationCount> findSumOperationCount(LocalDate targetDate) {
		LocalDate firstDay = targetDate.withDayOfMonth(1);

		return queryFactory
			.select(Projections.constructor(
				OperationCount.class,
				dailyStatisticEntity.bizId,
				dailyStatisticEntity.dailyDriveCount.sum()
			))
			.from(dailyStatisticEntity)
			.where(dailyStatisticEntity.date.between(firstDay, targetDate))
			.groupBy(dailyStatisticEntity.bizId)
			.fetch();
	}

	//차량 운행 시간의 총 합계
	@Override
	public List<OperationTime> findSumOperationTime(LocalDate targetDate) {
		LocalDate firstDay = targetDate.withDayOfMonth(1);

		return queryFactory
			.select(Projections.constructor(
				OperationTime.class,
				dailyStatisticEntity.bizId,
				dailyStatisticEntity.dailyDriveSec.sum()
			))
			.from(dailyStatisticEntity)
			.where(dailyStatisticEntity.date.between(firstDay, targetDate))
			.groupBy(dailyStatisticEntity.bizId)
			.fetch();
	}

	//차량 운행 거리의 총 합계
	@Override
	public List<OperationDistance> findSumOperationDistance(LocalDate targetDate) {
		LocalDate firstDay = targetDate.withDayOfMonth(1);

		return queryFactory
			.select(Projections.constructor(
				OperationDistance.class,
				dailyStatisticEntity.bizId,
				dailyStatisticEntity.dailyDriveDistance.sum()
			))
			.from(dailyStatisticEntity)
			.where(dailyStatisticEntity.date.between(firstDay, targetDate))
			.groupBy(dailyStatisticEntity.bizId)
			.fetch();
	}

	@Override
	public List<Integer> findDriveCountByBizUuid(String bizUuid) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDate startOfMonth = yesterday.withDayOfMonth(1);

		// 1. DB에서 실제 데이터 조회
		Map<LocalDate, Integer> driveCountMap = queryFactory
			.select(
				dailyStatisticEntity.date,
				dailyStatisticEntity.dailyDriveCount
			)
			.from(dailyStatisticEntity)
			.where(
				dailyStatisticEntity.biz.bizUuid.eq(bizUuid),
				dailyStatisticEntity.date.between(startOfMonth, yesterday)
			)
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(dailyStatisticEntity.date),
				tuple -> tuple.get(dailyStatisticEntity.dailyDriveCount)
			));

		// 2. 결과 리스트 생성 (날짜 순서대로)
		List<Integer> result = startOfMonth.datesUntil(yesterday.plusDays(1))
			.map(date -> driveCountMap.getOrDefault(date, 0))
			.collect(Collectors.toList());

		return result;
	}
}
