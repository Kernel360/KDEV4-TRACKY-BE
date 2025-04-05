package kernel360.trackyweb.car.infrastructure.repository;

import java.util.List;

import kernel360.trackycore.core.common.entity.CarEntity;

public interface CarRepositoryCustom {
	List<CarEntity> searchByFilter(String mdn, String status, String purpose);
}
