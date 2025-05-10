package kernel360.trackyweb.statistic.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackycore.core.domain.entity.QMonthlyStatisticEntity;
import kernel360.trackyweb.statistic.domain.dto.MonthlyStat;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MonthlyStatisticDomainRepositoryImpl implements MonthlyStatisticDomainRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	/**
	 * 월별 운행량 그래프 데이터 조회
	 * @param bizId
	 * @param currentDate
	 * @param targetDate targetDate BETWEEN currentDate 기간 조회
	 * @return
	 */
	@Override
	public List<MonthlyStat> getMonthlyStats(Long bizId, LocalDate currentDate, LocalDate targetDate) {
		QMonthlyStatisticEntity e = QMonthlyStatisticEntity.monthlyStatisticEntity;

		return queryFactory.select(
				Projections.constructor(
					MonthlyStat.class,
					e.date.year(),
					e.date.month(),
					e.totalDriveCount.sum(),
					e.totalDriveDistance.sum()
				))
			.from(e)
			.where(
				e.bizId.eq(bizId)
					.and(e.date.between(targetDate, currentDate))
			)
			.groupBy(e.date.yearMonth())
			.orderBy(e.date.yearMonth().asc())
			.fetch();
	}
}
