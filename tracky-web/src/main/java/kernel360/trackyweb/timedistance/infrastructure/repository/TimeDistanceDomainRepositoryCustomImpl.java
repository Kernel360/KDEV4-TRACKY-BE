package kernel360.trackyweb.timedistance.infrastructure.repository;

import static kernel360.trackycore.core.domain.entity.QBizEntity.bizEntity;
import static kernel360.trackycore.core.domain.entity.QTimeDistanceEntity.*;

import java.time.LocalDate;
import java.util.List;

import kernel360.trackyweb.admin.statistic.application.dto.response.HourlyGraphResponse;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackyweb.timedistance.application.dto.internal.OperationDistance;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationSeconds;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TimeDistanceDomainRepositoryCustomImpl implements TimeDistanceDomainRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Tuple> countByBizIdAndDateGroupedByHour(Long bizId, LocalDate date) {

		return queryFactory.select(timeDistanceEntity.hour, timeDistanceEntity.count())
			.from(timeDistanceEntity)
			.where(timeDistanceEntity.biz.id.eq(bizId)
				.and(timeDistanceEntity.date.eq(date))
			)
			.orderBy(timeDistanceEntity.hour.asc())
			.groupBy(timeDistanceEntity.hour)
			.orderBy(timeDistanceEntity.hour.asc())
			.fetch();
	}

	@Override
	public List<HourlyGraphResponse> getYesterdayData() {
		LocalDate yesterday = LocalDate.now().minusDays(1);

		return queryFactory
				.select(Projections.constructor(
						HourlyGraphResponse.class,
						timeDistanceEntity.hour,
						timeDistanceEntity.count()
				))
				.from(timeDistanceEntity)
				.where(timeDistanceEntity.date.eq(yesterday))
				.groupBy(timeDistanceEntity.hour)
				.fetch();
	}

	@Override
	public List<OperationSeconds> getDailyOperationTime(LocalDate targetDate) {
		return queryFactory
				.select(Projections.constructor(
						OperationSeconds.class,
						bizEntity.id,
						timeDistanceEntity.seconds.sum().coalesce(0) // 운행 시간이 없을 경우 0으로 처리
				))
				.from(bizEntity)
				.leftJoin(timeDistanceEntity).on(
						timeDistanceEntity.biz.id.eq(bizEntity.id),
						timeDistanceEntity.date.eq(targetDate)
				)
				.groupBy(bizEntity.id)
				.fetch();
	}

	@Override
	public List<OperationDistance> getDailyOperationDistance(LocalDate targetDate) {

		return queryFactory
				.select(Projections.constructor(
						OperationDistance.class,
						bizEntity.id,
						timeDistanceEntity.distance.sum().coalesce(0.0)
				))
				.from(bizEntity)
				.leftJoin(timeDistanceEntity)
				.on(
						timeDistanceEntity.biz.id.eq(bizEntity.id),
						timeDistanceEntity.date.eq(targetDate)
				)
				.groupBy(bizEntity.id)
				.fetch();
	}
}