package kernel360.trackyweb.timedistance.infrastructure.repository;

import static kernel360.trackycore.core.domain.entity.QTimeDistanceEntity.*;

import java.time.LocalDate;
import java.util.List;

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
}