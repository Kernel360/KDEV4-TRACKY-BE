package kernel360.trackyweb.car.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kernel360.trackycore.core.common.ApiResponse;
import kernel360.trackycore.core.domain.entity.CarEntity;
import kernel360.trackycore.core.domain.entity.DeviceEntity;
import kernel360.trackycore.core.infrastructure.exception.CarException;
import kernel360.trackycore.core.infrastructure.exception.DeviceException;
import kernel360.trackyweb.car.application.dto.CarDetailResponse;
import kernel360.trackyweb.car.application.dto.CarRequest;
import kernel360.trackyweb.car.application.dto.CarResponse;
import kernel360.trackyweb.car.infrastructure.repository.CarRepository;
import kernel360.trackyweb.car.infrastructure.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {

	private final CarRepository carRepository;
	private final DeviceRepository deviceRepository;

	/**
	 * 등록 차량 전체 조회
	 * @return 전체 차량 List
	 */
	public ApiResponse<List<CarResponse>> getAll() {
		return ApiResponse.success(carRepository.findAll().stream()
			.map(CarResponse::from)
			.collect(Collectors.toList()));
	}

	/**
	 * Mdn 기반 차량 검색
	 * @param keyword
	 * @return 검색된 차량 List
	 */
	public ApiResponse<List<CarResponse>> searchByMdn(String keyword) {
		return ApiResponse.success(carRepository.findByMdnContainingOrdered(keyword).stream()
			.map(CarResponse::from)
			.collect(Collectors.toList()));
	}

	/**
	 * 차량 ID 값으로 단건 검색
	 * @param id
	 * @return 단건 차량 데이터
	 */
	public ApiResponse<CarResponse> searchById(Long id) {
		CarEntity car = carRepository.findById(id)
			// 천승준 - 공통 에러 처리 해봤어요
			.orElseThrow(() -> CarException.notFound());
		return ApiResponse.success(CarResponse.from(car));
	}

	/**
	 * 디바이스 정보를 포함한 차량 ID 값으로 검색
	 * @param id
	 * @return 단건 차량 데이터 + 디바이스 정보
	 */
	public ApiResponse<CarDetailResponse> searchDetailById(Long id) {
		CarEntity car = carRepository.findDetailById(id)
			// 천승준 - 공통 에러 처리 해봤어요
			.orElseThrow(() -> CarException.notFound());
		return ApiResponse.success(CarDetailResponse.from(car));
	}

	/**
	 * 차량 신규 등록 ( device는 기본 device 설정 ID 1 가져옴 )
	 * @param carRequest
	 * @return 등록 성공한 차량 detail
	 */
	public ApiResponse<CarDetailResponse> create(CarRequest carRequest) {
		DeviceEntity device = deviceRepository.findById(1L)
			.orElseThrow(() -> DeviceException.notFound());

		CarEntity car = CarEntity.create(
			carRequest.mdn(),
			carRequest.bizId(),
			device,
			carRequest.carType(),
			carRequest.carPlate(),
			carRequest.carYear(),
			carRequest.purpose(),
			carRequest.status(),
			carRequest.sum()
		);

		var savedCar = carRepository.save(car);

		CarDetailResponse response = CarDetailResponse.from(savedCar);

		return ApiResponse.success(response);
	}


}
