package kernel360.trackyweb.rent.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.RentEntity;

@Repository
public interface RentRepository extends JpaRepository<RentEntity, Long>, RentRepositoryCustom {
	Optional<RentEntity> searchByRentUuid(String rentUuid);
	Optional<RentEntity> findByRentUuid(String rentUuid);
	void deleteByRentUuid(String rentUuid);
	Optional<RentEntity> findDetailByRentUuid(String rentUuid);

	/** rentUuid로 차량 검색 + 결과값의 앞부분이 검색어와 일치할수록 앞으로 정렬
	 * @param rentUuid
	 * @return 검색 대여 list
	 */

	@Query("SELECT r FROM RentEntity r " + "WHERE LOWER(r.rentUuid) LIKE LOWER(CONCAT('%', :rentUuid, '%')) "
		+ "ORDER BY LOCATE(LOWER(:rentUuid), LOWER(r.rentUuid)) ASC")
	List<RentEntity> findByRentUuidContainingOrdered(@Param("rentUuid") String rentUuid);

}
