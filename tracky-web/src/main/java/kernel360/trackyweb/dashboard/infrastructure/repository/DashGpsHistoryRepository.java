package kernel360.trackyweb.dashboard.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.common.entity.GpsHistoryEntity;
import kernel360.trackycore.core.common.entity.GpsHistoryId;

@Repository
public interface DashGpsHistoryRepository extends JpaRepository<GpsHistoryEntity, GpsHistoryId> {

	@Query(value = """
		WITH latest_gps AS (
		    SELECT 
		        g.*, 
		        d.mdn,
		        ROW_NUMBER() OVER (PARTITION BY d.mdn ORDER BY g.created_at DESC) AS rn
		    FROM gpshistory g
		    JOIN drive d ON g.drive_id = d.id
		    WHERE d.id IN (
		        SELECT MAX(id)
		        FROM drive
		        GROUP BY mdn
		    )
		)
		SELECT *
		FROM latest_gps
		WHERE rn = 1
		""", nativeQuery = true)
	List<GpsHistoryEntity> findLatestGpsByMdn();

}
