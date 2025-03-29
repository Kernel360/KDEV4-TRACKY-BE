package kernel360.trackyweb.car.application.dto;

import java.time.LocalDateTime;

import kernel360.trackycore.core.domain.entity.CarEntity;
import kernel360.trackycore.core.domain.entity.DeviceEntity;

public record CarDetailResponse(
	Long id,
	String mdn,
	String bizName,
	String carType,
	String carPlate,
	String carYear,
	String purpose,
	String status,
	String sum,
	DeviceEntity deviceInfo,
	LocalDateTime createdAt
) {
	public static CarDetailResponse from(CarEntity car) {
		return new CarDetailResponse(
			car.getId(),
			car.getMdn(),
			car.getBizId(),
			car.getCarType(),
			car.getCarPlate(),
			car.getCarYear(),
			car.getPurpose(),
			car.getStatus(),
			car.getSum(),
			car.getDevice(),
			car.getCreatedAt()
		);
	}
}