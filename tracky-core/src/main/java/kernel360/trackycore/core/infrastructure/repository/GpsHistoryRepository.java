package kernel360.trackycore.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel360.trackycore.core.infrastructure.entity.GpsHistoryEntity;

public interface GpsHistoryRepository extends JpaRepository<GpsHistoryEntity, Long> {
}
