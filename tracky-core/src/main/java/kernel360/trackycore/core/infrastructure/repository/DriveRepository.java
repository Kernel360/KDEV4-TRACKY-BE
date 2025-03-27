package kernel360.trackycore.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.infrastructure.entity.DriveEntity;

@Repository
public interface DriveRepository extends JpaRepository<DriveEntity, Long> {
	DriveEntity findByMdn(String mdn);
}