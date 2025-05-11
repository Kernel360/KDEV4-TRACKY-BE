package kernel360.trackyweb.statistic.infrastructure.repository.monthly;

import static kernel360.trackycore.core.domain.entity.QDailyStatisticEntity.dailyStatisticEntity;
import static kernel360.trackycore.core.domain.entity.QMonthlyStatisticEntity.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackycore.core.domain.entity.QDailyStatisticEntity;
import kernel360.trackycore.core.domain.entity.QMonthlyStatisticEntity;
import kernel360.trackyweb.admin.statistic.application.dto.response.GraphsResponse;
import kernel360.trackyweb.admin.statistic.application.dto.response.MonthlyDriveCountResponse;
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
     *
     * @param bizId
     * @param currentDate
     * @param targetDate  targetDate BETWEEN currentDate 기간 조회
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

    @Override
    public List<GraphsResponse.NonOperatedCar> getNonOperatedCarWithBizName() {
        QMonthlyStatisticEntity sub = new QMonthlyStatisticEntity("sub");

        return queryFactory
                .select(Projections.constructor(
                                GraphsResponse.NonOperatedCar.class,
                                monthlyStatisticEntity.biz.bizName,
                                monthlyStatisticEntity.nonOperatingCarCount
                        )
                )
                .from(monthlyStatisticEntity)
                .where(
                        monthlyStatisticEntity.date.eq(
                                JPAExpressions
                                        .select(sub.date.max())
                                        .from(sub)
                                        .where(sub.biz.id.eq(monthlyStatisticEntity.biz.id))
                        )
                )
                .orderBy(monthlyStatisticEntity.nonOperatingCarCount.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<GraphsResponse.DriveCount> getTotalDriveCount() {
        LocalDate thisMonth = LocalDate.now().minusDays(1);
        LocalDate sixMonthsAgo =YearMonth.now().minusMonths(6).atEndOfMonth();

        return queryFactory
                .select(Projections.constructor(
                        GraphsResponse.DriveCount.class,
                        monthlyStatisticEntity.date,
                        monthlyStatisticEntity.totalDriveCount.sum()
                ))
                .from(monthlyStatisticEntity)
                .where(monthlyStatisticEntity.date.between(sixMonthsAgo, thisMonth))
                .groupBy(monthlyStatisticEntity.date)
                .orderBy(monthlyStatisticEntity.date.desc())
                .limit(6)
                .fetch();
    }

    @Override
    public List<MonthlyStatisticEntity> getTotalDriveCountInOneYear(String bizName) {
        LocalDate thisMonth = LocalDate.now().minusDays(1);
        LocalDate twelveMonthsAgo = YearMonth.now().minusMonths(12).atEndOfMonth();

        return queryFactory
                .selectFrom(monthlyStatisticEntity)
                .where(
                        monthlyStatisticEntity.biz.bizName.eq(bizName)
                                .and(monthlyStatisticEntity.date.between(twelveMonthsAgo, thisMonth))
                )
                .fetch();
    }
}
