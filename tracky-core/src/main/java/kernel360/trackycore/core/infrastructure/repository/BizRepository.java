package kernel360.trackycore.core.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel360.trackycore.core.domain.entity.BizEntity;

public interface BizRepository extends JpaRepository<BizEntity, Long> {
	Optional<BizEntity> findByBizUuid(String bizUuid);

	Optional<BizEntity> findByBizName(String bizName);
}
