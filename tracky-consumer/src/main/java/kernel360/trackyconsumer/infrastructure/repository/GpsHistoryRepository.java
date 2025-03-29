package kernel360.trackyconsumer.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.infrastructure.entity.GpsHistoryEntity;

@Repository
public interface GpsHistoryRepository extends JpaRepository<GpsHistoryEntity, Long> {
}
