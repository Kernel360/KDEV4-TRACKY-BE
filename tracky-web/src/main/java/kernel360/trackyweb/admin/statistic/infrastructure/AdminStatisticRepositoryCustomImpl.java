package kernel360.trackyweb.admin.statistic.infrastructure;

import static kernel360.trackycore.core.domain.entity.QBizEntity.*;
import static kernel360.trackycore.core.domain.entity.QCarEntity.*;
import static kernel360.trackycore.core.domain.entity.QDriveEntity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackyweb.admin.statistic.application.dto.AdminBizListResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminStatisticRepositoryCustomImpl implements AdminStatisticRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<AdminBizListResponse> fetchAdminBizList(LocalDate selectedDate) {
		LocalDateTime start = selectedDate.atStartOfDay();
		LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

		return queryFactory
			.select(Projections.constructor(
				AdminBizListResponse.class,
				bizEntity.bizName,
				carEntity.mdn.countDistinct().intValue(),
				// 운행차량 수: driveOffTime 조건은 ON 절에 포함
				driveEntity.car.mdn.countDistinct().intValue(),
				// 오류율: 운행 레코드가 없으면 sum()이 null → coalesce로 0 처리
				driveEntity.skipCount.sum().coalesce(0).intValue()
			))
			.from(bizEntity)
			.leftJoin(carEntity)
			.on(carEntity.biz.id.eq(bizEntity.id))
			.leftJoin(driveEntity)
			.on(driveEntity.car.mdn.eq(carEntity.mdn)
				.and(
					driveEntity.driveOffTime.isNull()
						.or(driveEntity.driveOffTime.between(start, end))
				)
			)
			.groupBy(bizEntity.id)
			.fetch();
	}

	@Override
	public List<Tuple> findTotalCarCountAndBizName() {
		return queryFactory
			.select(
				bizEntity.bizName,
				carEntity.countDistinct()
			)
			.from(bizEntity)
			.leftJoin(carEntity)
			.on(carEntity.biz.id.eq(bizEntity.id))
			.groupBy(bizEntity.id)
			.fetch();
	}

	@Override
	public List<Tuple> countRunningDriveAndSkipCount(LocalDate selectedDate) {
		LocalDateTime start = selectedDate.atStartOfDay();
		LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

		return queryFactory
			.select(
				driveEntity.countDistinct(),
				driveEntity.skipCount.sum().intValue()
			)
			.from(driveEntity)
			.where(driveEntity.driveOffTime.isNull()
				.or(driveEntity.driveOffTime.goe(start)
					.and(driveEntity.driveOffTime.lt(end)))
			)
			.groupBy(driveEntity.car.biz.id)
			.fetch();
	}
}
