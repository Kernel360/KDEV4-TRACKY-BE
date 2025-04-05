package kernel360.trackyweb.car.presentation.dto;

import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.DeviceEntity;

public record CarUpdateRequest(
	Long bizId,
	DeviceEntity device,
	String carType,
	String carPlate,
	String carYear,
	String purpose,
	String status,
	double sum
) {
	public static CarUpdateRequest from(CarEntity car) {
		return new CarUpdateRequest(
			car.getBizId(),
			car.getDevice(),
			car.getCarType(),
			car.getCarPlate(),
			car.getCarYear(),
			car.getPurpose(),
			car.getStatus(),
			car.getSum()
		);
	}
}
