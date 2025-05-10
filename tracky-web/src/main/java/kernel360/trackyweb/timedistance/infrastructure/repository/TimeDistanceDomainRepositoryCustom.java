package kernel360.trackyweb.timedistance.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.Tuple;

import kernel360.trackyweb.timedistance.application.dto.internal.OperationDistance;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationSeconds;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizStatisticResponse;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationTime;

public interface TimeDistanceDomainRepositoryCustom {
	List<OperationSeconds> getDailyOperationTime(LocalDate targetDate);

	List<OperationDistance> getDailyOperationDistance(LocalDate targetDate);

	List<OperationTime> getTotalOperationTimeGroupedByBIzId(LocalDate date);

	List<Tuple> countByBizIdAndDateGroupedByHour(Long bizId, LocalDate date);

	AdminBizStatisticResponse getDriveStatByBizIdAndDate(Long bizId, LocalDate selectedDate);

}
