package kernel360.trackyweb.rent.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import kernel360.trackycore.core.common.entity.RentEntity;

public interface RentRepositoryCustom {
	List<RentEntity> searchByFilters(String rentUuid, String rentStatus, LocalDateTime rentDate);
}

