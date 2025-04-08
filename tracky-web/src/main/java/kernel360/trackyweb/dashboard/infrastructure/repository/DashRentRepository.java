package kernel360.trackyweb.dashboard.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kernel360.trackycore.core.common.entity.RentEntity;

public interface DashRentRepository extends JpaRepository<RentEntity, Long> {
	List<RentEntity> findByRentStimeBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, r.rentStime, r.rentEtime)) FROM RentEntity r WHERE r.rentStime IS NOT NULL AND r.rentEtime IS NOT NULL")
	Long getTotalRentDurationInMinutes();
}
