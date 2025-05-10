package kernel360.trackyweb.statistic.infrastructure.repository.monthly;

import static kernel360.trackycore.core.domain.entity.QMonthlyStatisticEntity.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackycore.core.domain.entity.MonthlyStatisticEntity;
import kernel360.trackyweb.statistic.application.dto.response.MonthlyStatisticResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MonthlyStatisticRepositoryCustomImpl implements MonthlyStatisticRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public MonthlyStatisticEntity findLatestMonthlyStatistic(String bizUuid) {
		return queryFactory
			.selectFrom(monthlyStatisticEntity)
			.where(monthlyStatisticEntity.biz.bizUuid.eq(bizUuid))
			.orderBy(monthlyStatisticEntity.date.desc())
			.limit(1)
			.fetchOne();
	}

	/**
	 * 월별 운행량 그래프 데이터 조회
	 * @param bizId
	 * @param currentDate
	 * @param targetDate targetDate BETWEEN currentDate 기간 조회
	 * @return
	 */
	@Override
	public List<MonthlyStatisticResponse.MonthlyStats> getMonthlyStats(Long bizId, LocalDate currentDate,
		LocalDate targetDate) {
		return queryFactory
			.select(
				Projections.constructor(
					MonthlyStatisticResponse.MonthlyStats.class,
					monthlyStatisticEntity.date.year(),
					monthlyStatisticEntity.date.month(),
					monthlyStatisticEntity.totalDriveCount.sum(),
					monthlyStatisticEntity.totalDriveDistance.sum()
				))
			.from(monthlyStatisticEntity)
			.where(
				monthlyStatisticEntity.bizId.eq(bizId)
					.and(monthlyStatisticEntity.date.between(targetDate, currentDate))
			)
			.groupBy(monthlyStatisticEntity.date.yearMonth())
			.orderBy(monthlyStatisticEntity.date.yearMonth().asc())
			.fetch();
	}
}
