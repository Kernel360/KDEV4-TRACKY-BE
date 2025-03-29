package kernel360.trackyweb.car.application.dto;

import java.time.LocalDateTime;

import kernel360.trackycore.core.domain.entity.CarEntity;

public record CarResponse(
	Long id,
	String mdn,
	String bizId,
	String carType,
	String carPlate,
	String carYear,
	String purpose,
	String status,
	String sum,
	LocalDateTime createdAt
) {
	public static CarResponse from(CarEntity car) {
		return new CarResponse(
			car.getId(),
			car.getMdn(),
			car.getBizId(),
			car.getCarType(),
			car.getCarPlate(),
			car.getCarYear(),
			car.getPurpose(),
			car.getStatus(),
			car.getSum(),
			car.getCreatedAt()
		);
	}
}

