package kernel360.trackyweb.rent.infrastructure.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kernel360.trackycore.core.domain.entity.RentEntity;

public interface RentRepositoryCustom {
	Page<RentEntity> searchByFilter(String rentUuid, String rentStatus, LocalDateTime rentDate, Pageable pageable);
}

