package kernel360.trackyweb.car.application.dto;

import kernel360.trackycore.core.domain.entity.CarEntity;

public record CarRequest(
	String mdn,
	String bizId,
	String carType,
	String carPlate,
	String carYear,
	String purpose,
	String status,
	String sum
) {
	public static CarRequest from(CarEntity car) {
		return new CarRequest(
			car.getMdn(),
			car.getBizId(),
			car.getCarType(),
			car.getCarPlate(),
			car.getCarYear(),
			car.getPurpose(),
			car.getStatus(),
			car.getSum()
		);
	}
}
