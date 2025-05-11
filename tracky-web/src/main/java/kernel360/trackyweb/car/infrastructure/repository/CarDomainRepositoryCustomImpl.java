package kernel360.trackyweb.car.infrastructure.repository;

import static kernel360.trackycore.core.domain.entity.QBizEntity.*;
import static kernel360.trackycore.core.domain.entity.QCarEntity.*;
import static kernel360.trackycore.core.domain.entity.QRentEntity.*;
import static kernel360.trackycore.core.domain.entity.QTimeDistanceEntity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import kernel360.trackycore.core.domain.entity.QBizEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackycore.core.domain.entity.CarEntity;
import kernel360.trackycore.core.domain.entity.enums.CarStatus;
import kernel360.trackycore.core.domain.entity.enums.CarType;
import kernel360.trackyweb.car.application.dto.internal.CarCountWithBizId;
import kernel360.trackyweb.statistic.application.dto.response.CarStatisticResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CarDomainRepositoryCustomImpl implements CarDomainRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<String> findAllMdnByBizId(String bizUuid) {
		return queryFactory
			.select(carEntity.mdn)
			.from(carEntity)
			.join(carEntity.biz, bizEntity)
			.where(carEntity.biz.bizUuid.eq(bizUuid))
			.fetch();
	}

	@Override
	public Page<CarEntity> searchCarByFilter(String bizUuid, String search, CarStatus status, CarType carType,
		Pageable pageable) {



		BooleanBuilder builder = new BooleanBuilder()
			.and(carEntity.biz.bizUuid.eq(bizUuid))
			.and(isContainsCarMdnOrCarPlate(search))
			.and(isEqualStatus(status))
			.and(isEqualCarType(carType))
			.and(isNotDeleted(status));

		JPAQuery<CarEntity> query = queryFactory
			.selectFrom(carEntity)
			.where(builder)
			.orderBy(carPlateSort(search));

		List<CarEntity> content = fetchPagedContent(query, pageable);

		long total = Optional.ofNullable(
			queryFactory
				.select(carEntity.count())
				.from(carEntity)
				.where(builder)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<CarEntity> searchDriveCarByFilter(String bizUuid, String search, Pageable pageable) {

		JPAQuery<CarEntity> query = queryFactory
			.select(carEntity)
			.from(carEntity)
			.join(carEntity.biz, bizEntity)
			.where(carEntity.biz.bizUuid.eq(bizUuid)
				.and(isContainsCarMdnOrCarPlate(search)));

		List<CarEntity> content = fetchPagedContent(query, pageable);

		long total = Optional.ofNullable(
			queryFactory
				.select(carEntity.count())
				.from(carEntity)
				.join(carEntity.biz, bizEntity)
				.where(bizEntity.bizUuid.eq(bizUuid)
					.and(isContainsCarMdnOrCarPlate(search)))
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public List<CarEntity> findAllByBizUuid(String bizUuid) {
		return queryFactory
			.selectFrom(carEntity)
			.join(carEntity.biz, bizEntity)
			.where(bizEntity.bizUuid.eq(bizUuid))
			.fetch();
	}

	@Override
	public List<CarCountWithBizId> getDailyTotalCarCount() {
		return queryFactory
				.select(Projections.constructor(
						CarCountWithBizId.class,
						bizEntity.id,
						carEntity.count().coalesce(0L) // car가 없을 경우 0으로 처리
				))
				.from(bizEntity)
				.leftJoin(carEntity).on(carEntity.biz.id.eq(bizEntity.id))
				.groupBy(bizEntity.id)
				.fetch();
	}

	@Override
	public List<CarEntity> availableEmulate(String bizUuid) {
		LocalDateTime now = LocalDateTime.now();
		return queryFactory
			.select(carEntity)
			.from(carEntity)
			.join(rentEntity).on(rentEntity.car.eq(carEntity)) // 반드시 rent가 연결된 경우만
			.where(
				carEntity.biz.bizUuid.eq(bizUuid),
				rentEntity.rentStime.loe(now),
				rentEntity.rentEtime.goe(now)
			)
			.fetch();
	}

	// 통계 - 차량별 상세 통계
	@Override
	public Page<CarStatisticResponse> searchCarStatisticByFilter(Long bizId, String search, Pageable pageable) {

		NumberExpression<Integer> totalSeconds = timeDistanceEntity.seconds.sum().coalesce(0);

		NumberExpression<Double> totalDistance = timeDistanceEntity.distance.sum().coalesce(0.0);

		NumberExpression<Integer> avgSpeed
			= totalDistance
			.multiply(3.6)
			.divide(totalSeconds).intValue();

		BooleanBuilder builder = new BooleanBuilder()
			.and(carEntity.biz.id.eq(bizId))
			.and(carEntity.status.ne(CarStatus.DELETED))
			.and(isContainsCarMdnOrCarPlate(search));

		List<CarStatisticResponse> carStatisticResponses = queryFactory
			.select(Projections.constructor(
				CarStatisticResponse.class,
				carEntity.mdn,
				carEntity.carPlate,
				carEntity.carType,
				totalSeconds,
				totalDistance,
				avgSpeed
			))
			.from(carEntity)
			.leftJoin(timeDistanceEntity)
			.on(timeDistanceEntity.car.mdn.eq(carEntity.mdn))
			.where(builder)
			.groupBy(carEntity.mdn)
			.orderBy(carPlateSort(search))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(
			queryFactory
				.select(carEntity.mdn.count())
				.from(carEntity)
				.where(builder)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(carStatisticResponses, pageable, total);
	}

	//검색 조건
	private BooleanExpression isContainsCarMdnOrCarPlate(String search) {
		if (StringUtils.isBlank(search)) {
			return null;
		}
		return carEntity.mdn.containsIgnoreCase(search)
			.or(carEntity.carPlate.containsIgnoreCase(search));
	}

	private BooleanExpression isEqualStatus(CarStatus status) {
		if (status == null) {
			return null;
		}
		return carEntity.status.eq(status);
	}

	private BooleanExpression isEqualCarType(CarType carType) {
		if (carType == null) {
			return null;
		}
		return carEntity.carType.eq(carType);
	}

	private BooleanExpression isNotDeleted(CarStatus status) {
		if (status == CarStatus.DELETED) {
			return null;
		}
		return carEntity.status.ne(CarStatus.DELETED);
	}

	//정렬 조건
	private OrderSpecifier<?>[] carPlateSort(String search) {
		if (StringUtils.isNotBlank(search)) {
			NumberTemplate<Integer> carPlatePriority = Expressions.numberTemplate(
				Integer.class, "CASE WHEN {1} LIKE CONCAT('%', {0}, '%') THEN 0 ELSE 1 END",
				search, carEntity.carPlate
			);

			NumberTemplate<Integer> carPlatePos = Expressions.numberTemplate(
				Integer.class, "LOCATE({0}, {1})", search, carEntity.carPlate
			);

			return new OrderSpecifier<?>[] {
				carPlatePriority.asc(),
				carPlatePos.asc(),
				carEntity.carPlate.asc()
			};
		}
		return new OrderSpecifier<?>[] {carEntity.carPlate.asc()};
	}

	private <T> List<T> fetchPagedContent(JPAQuery<T> query, Pageable pageable) {
		return query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

}
