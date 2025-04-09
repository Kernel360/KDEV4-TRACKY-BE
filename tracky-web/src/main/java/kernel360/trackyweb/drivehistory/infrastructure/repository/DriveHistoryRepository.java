package kernel360.trackyweb.drivehistory.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel360.trackycore.core.common.entity.DriveEntity;

public interface DriveHistoryRepository extends JpaRepository<DriveEntity, Long> {
	List<DriveEntity> findAllByDriveId(Long id);
}
