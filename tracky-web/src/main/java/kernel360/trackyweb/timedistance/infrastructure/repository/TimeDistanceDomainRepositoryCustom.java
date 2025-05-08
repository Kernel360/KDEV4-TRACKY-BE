package kernel360.trackyweb.timedistance.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.Tuple;

import kernel360.trackyweb.timedistance.application.dto.internal.OperationDistance;
import kernel360.trackyweb.timedistance.application.dto.internal.OperationSeconds;

public interface TimeDistanceDomainRepositoryCustom {
	List<OperationSeconds> getDailyOperationTime(LocalDate targetDate);

	List<OperationDistance> getDailyOperationDistance(LocalDate targetDate);

	List<Tuple> countByBizIdAndDateGroupedByHour(Long bizId, LocalDate targetDate);
}
