package kernel360.trackycore.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel360.trackycore.core.common.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
}
