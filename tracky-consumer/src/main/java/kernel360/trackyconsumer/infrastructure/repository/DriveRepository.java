package kernel360.trackyconsumer.infrastructure.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.infrastructure.entity.DriveEntity;

@Repository
public interface DriveRepository extends JpaRepository<DriveEntity, Long> {

    @Query("SELECT d FROM DriveEntity d WHERE d.mdn = :mdn " +
           "AND d.driveOnTime <= :otime " +
           "ORDER BY d.id DESC LIMIT 1")
    DriveEntity findByMdnAndOtime(String mdn, LocalDateTime otime);

	DriveEntity findByMdn(String mdn);
}