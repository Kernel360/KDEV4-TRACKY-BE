package kernel360.trackyweb.car.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.domain.entity.CarEntity;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {

	/**
	 * Mdn 으로 차량 검색 + 검색어를 포함한 모든 차량 + 결과값의 앞부분이 검색어와 일치할수록 앞으로 정렬
	 * @param keyword 검색어
	 * @return 검색된 차량 List
	 */
	@Query("SELECT c FROM CarEntity c " + "WHERE LOWER(c.mdn) LIKE LOWER(CONCAT('%', :keyword, '%')) "
		+ "ORDER BY LOCATE(LOWER(:keyword), LOWER(c.mdn)) ASC")
	List<CarEntity> findByMdnContainingOrdered(@Param("keyword") String keyword);

	/**
	 * 디바이스 내용을 포함한 차량 단건 데이터
	 * @param id 차량 ID
	 * @return 차량 단건 조회 값
	 */
	@Query("SELECT c FROM CarEntity c JOIN FETCH c.device WHERE c.id = :id")
	Optional<CarEntity> findDetailById(@Param("id") Long id);
}
