package kernel360.trackyconsumer.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel360.trackycore.core.infrastructure.entity.CarEntity;

@Repository
public interface CarEntityRepository extends JpaRepository<CarEntity, Long> {

	CarEntity findByMdn(String mdn);
}
