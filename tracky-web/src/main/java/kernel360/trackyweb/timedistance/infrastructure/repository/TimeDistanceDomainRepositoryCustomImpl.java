package kernel360.trackyweb.timedistance.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackycore.core.domain.entity.QTimeDistanceEntity;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationDistance;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationSeconds;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizStatisticResponse;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationTime;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TimeDistanceDomainRepositoryCustomImpl implements TimeDistanceDomainRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<OperationTime> getTotalOperationTimeGroupedByBIzId(LocalDate date) {
		return List.of();
	}

	@Override
	public List<Tuple> countByBizIdAndDateGroupedByHour(Long bizId, LocalDate date) {

		return queryFactory.select(timeDistanceEntity.hour, timeDistanceEntity.count())
			.from(timeDistanceEntity)
			.where(timeDistanceEntity.biz.id.eq(bizId)
				.and(timeDistanceEntity.date.eq(date))
			)
			.orderBy(e.hour.asc())
			.groupBy(timeDistanceEntity.hour)
			.orderBy(timeDistanceEntity.hour.asc())
			.fetch();
	}

	@Override
	public List<OperationSeconds> getDailyOperationTime(LocalDate targetDate) {
		return queryFactory
			.select(Projections.constructor(
				OperationSeconds.class,
				timeDistanceEntity.biz.id,
				timeDistanceEntity.seconds.sum()
			))
			.from(timeDistanceEntity)
			.where(timeDistanceEntity.date.eq(targetDate))
			.groupBy(timeDistanceEntity.biz.id)
			.fetch();

	}

	@Override
	public List<OperationDistance> getDailyOperationDistance(LocalDate targetDate) {
		return queryFactory
			.select(Projections.constructor(
				OperationDistance.class,
				timeDistanceEntity.biz.id,
				timeDistanceEntity.distance.sum()
			))
			.from(timeDistanceEntity)
			.where(timeDistanceEntity.date.eq(targetDate))
			.groupBy(timeDistanceEntity.biz.id)
			.fetch();
	}

	private BooleanExpression isEqualsBizId(Long bizId) {
		if (bizId == null) {
			return Expressions.TRUE;
		}
		return timeDistanceEntity.biz.id.eq(bizId);
	}
}