package kernel360.trackyweb.drive.infrastructure.repository;

import static kernel360.trackycore.core.domain.entity.QBizEntity.bizEntity;
import static kernel360.trackycore.core.domain.entity.QCarEntity.*;
import static kernel360.trackycore.core.domain.entity.QDriveEntity.*;
import static kernel360.trackycore.core.domain.entity.QGpsHistoryEntity.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import kernel360.trackycore.core.domain.entity.QBizEntity;
import kernel360.trackycore.core.domain.entity.QCarEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackycore.core.domain.entity.DriveEntity;
import kernel360.trackycore.core.domain.entity.QDriveEntity;
import kernel360.trackyweb.drive.application.dto.internal.NonOperatedCar;
import kernel360.trackyweb.drive.application.dto.internal.OperationCarCount;
import kernel360.trackyweb.drive.application.dto.internal.OperationTotalCount;
import kernel360.trackyweb.drive.domain.vo.DriveHistory;
import kernel360.trackyweb.drive.domain.vo.GpsData;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DriveDomainRepositoryImpl implements DriveDomainRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // MDN 또는 날짜 필터로 주행기록 검색 (날짜 범위 없으면 최신 20건)
    @Override
    public Page<DriveEntity> searchByFilter(String search, String mdn, LocalDate startDate, LocalDate endDate,
                                            Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();

        // 날짜 구간은 무조건 필터링
        condition.and(driveEntity.driveOnTime.between(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1)));
        condition.and(isEqualMdnContainsRenterName(mdn, search));

        // 메인 쿼리
        List<DriveEntity> content = queryFactory
                .selectFrom(driveEntity)
                .where(condition)
                .orderBy(driveEntity.driveOnTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        long total = Optional.ofNullable(
                queryFactory
                        .select(driveEntity.count())
                        .from(driveEntity)
                        .where(condition)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    // 현재 주행중인 차량 목록 조회 (차량별로 하나만 유지 + 검색 포함)
    @Override
    public Page<DriveEntity> findRunningDriveList(String search, Pageable pageable) {

        // 주행 중인 차량 리스트 가져오기
        List<DriveEntity> fetchedDrives = queryFactory
                .selectFrom(driveEntity)
                .join(driveEntity.car)
                .fetchJoin()
                .where(buildRunningDriveCondition(search))
                .orderBy(driveEntity.driveOnTime.desc())
                .fetch();

        // 차량(mdn) 당 하나만 유지
        Map<String, DriveEntity> distinctByMdn = fetchedDrives.stream()
                .collect(Collectors.toMap(
                        drive -> drive.getCar().getMdn(),
                        Function.identity(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        List<DriveEntity> distinctDrives = new ArrayList<>(distinctByMdn.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), distinctDrives.size());
        List<DriveEntity> pagedDrives = (start < end) ? distinctDrives.subList(start, end) : Collections.emptyList();

        return new PageImpl<>(pagedDrives, pageable, distinctDrives.size());
    }

    // 특정 주행 ID로 주행 중인 항목 단건 조회
    @Override
    public Optional<DriveEntity> findRunningDriveById(Long driveId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QDriveEntity.driveEntity)
                        .join(QDriveEntity.driveEntity.car).fetchJoin()
                        .join(QDriveEntity.driveEntity.rent).fetchJoin()
                        .where(
                                QDriveEntity.driveEntity.id.eq(driveId),
                                QDriveEntity.driveEntity.driveOffTime.isNull()
                        )
                        .fetchFirst()
        );
    }

    @Override
    public List<DriveEntity> findDriveListByMdn(String mdn) {
        return queryFactory
                .selectFrom(driveEntity)
                .where(driveEntity.car.mdn.eq(mdn))
                .fetch();
    }

    private boolean isUnboundedSearch(LocalDateTime start, LocalDateTime end) {
        return start == null && end == null;
    }

    @Override
    public Optional<DriveHistory> findByDriveId(Long driveId) {

        // drive 기본 정보 조회
        DriveEntity drive = queryFactory
                .selectFrom(driveEntity)
                .distinct()
                .join(driveEntity.car).fetchJoin()
                .join(driveEntity.rent).fetchJoin()
                .join(driveEntity.location).fetchJoin()
                .where(driveEntity.id.eq(driveId))
                .fetchOne();

        if (drive == null) {
            return Optional.empty();
        }

        // gps 이력 리스트 조회
        List<GpsData> gpsDataList = queryFactory
                .selectFrom(gpsHistoryEntity)
                .where(gpsHistoryEntity.drive.id.eq(driveId))
                .orderBy(gpsHistoryEntity.oTime.asc())
                .fetch()
                .stream()
                .map(gps -> GpsData.create(
                        gps.getLat(),
                        gps.getLon(),
                        gps.getSpd(),
                        gps.getAng(),
                        gps.getOTime()
                ))
                .toList();

        DriveHistory driveHistory = DriveHistory.create(
                drive, gpsDataList
        );

        return Optional.of(driveHistory);
    }

    @Override
    public List<OperationCarCount> getDailyOperationCar(LocalDate targetDate) {
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.plusDays(1).atStartOfDay();

        return queryFactory
                .select(Projections.constructor(
                        OperationCarCount.class,
                        bizEntity.id,
                        driveEntity.car.mdn.countDistinct().coalesce(0L)
                ))
                .from(bizEntity)
                .leftJoin(carEntity).on(carEntity.biz.id.eq(bizEntity.id))
                .leftJoin(driveEntity).on(
                        driveEntity.car.mdn.eq(carEntity.mdn),
                        driveEntity.driveOnTime.between(start, end.minusNanos(1))
                )
                .groupBy(bizEntity.id)
                .fetch();
    }

    //일일 통계 - target Date의 운행 총 횟수
    @Override
    public List<OperationTotalCount> getDailyTotalOperation(LocalDate targetDate) {
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.plusDays(1).atStartOfDay().minusNanos(1);

        return queryFactory
                .select(Projections.constructor(
                        OperationTotalCount.class,
                        bizEntity.id,
                        driveEntity.count().coalesce(0L)
                ))
                .from(bizEntity)
                .leftJoin(driveEntity)
                .on(
                        driveEntity.car.biz.id.eq(bizEntity.id),
                        driveEntity.driveOnTime.between(start, end)
                )
                .groupBy(bizEntity.id)
                .fetch();
    }

    //월별 통계 - 미운행 차량 수
    @Override
    public List<NonOperatedCar> getNonOperatedCars(LocalDate targetDate) {
        LocalDateTime monthStart = targetDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime targetEnd = targetDate.plusDays(1).atStartOfDay();

        return queryFactory
                .select(Projections.constructor(
                        NonOperatedCar.class,
                        bizEntity.id,
                        carEntity.count().coalesce(0L)
                ))
                .from(bizEntity)
                .leftJoin(carEntity).on(carEntity.biz.id.eq(bizEntity.id))
                .leftJoin(driveEntity).on(
                        driveEntity.car.eq(carEntity),
                        driveEntity.driveOnTime.between(monthStart, targetEnd)
                )
                .where(driveEntity.isNull())
                .groupBy(bizEntity.id)
                .fetch();
    }

    private BooleanBuilder buildRunningDriveCondition(String search) {
        return new BooleanBuilder()
                .and(driveEntity.driveOffTime.isNull())
                .and(buildSearchKeywordCondition(search));
    }

    private BooleanBuilder buildSearchKeywordCondition(String search) {
        if (StringUtils.isBlank(search))
            return new BooleanBuilder();

        return new BooleanBuilder()
                .or(driveEntity.car.mdn.containsIgnoreCase(search))
                .or(driveEntity.car.carPlate.containsIgnoreCase(search));
    }

    private long fetchTotalCount(BooleanBuilder condition) {
        return Optional.ofNullable(
                queryFactory
                        .select(driveEntity.count())
                        .from(driveEntity)
                        .where(condition)
                        .fetchOne()
        ).orElse(0L);
    }

    //검색 조건
    private BooleanExpression isEqualMdnContainsRenterName(String mdn, String search) {
        if (StringUtils.isBlank(search)) {
            return driveEntity.car.mdn.eq(mdn);
        }
        return driveEntity.car.mdn.eq(mdn)
                .and(driveEntity.rent.renterName.containsIgnoreCase(search));
    }

}
