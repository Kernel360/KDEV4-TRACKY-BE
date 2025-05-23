package kernel360.trackyweb.car.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import kernel360.trackycore.core.domain.entity.CarEntity;

public record CarResponse(
	String mdn,
	Long bizId,
	String carType,
	String carPlate,
	String carYear,
	String purpose,
	String status,
	double sum,
	LocalDateTime createdAt
) {
	public static CarResponse from(CarEntity car) {
		return new CarResponse(
			car.getMdn(),
			car.getBiz().getId(),
			car.getCarType(),
			car.getCarPlate(),
			car.getCarYear(),
			car.getPurpose(),
			car.getStatus(),
			car.getSum(),
			car.getCreatedAt()
		);
	}

	public static List<CarResponse> fromList(List<CarEntity> cars) {
		return cars.stream()
			.map(CarResponse::from)
			.toList();
	}
}

