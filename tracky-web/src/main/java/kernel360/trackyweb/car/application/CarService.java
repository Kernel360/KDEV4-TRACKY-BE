package kernel360.trackyweb.car.application;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.DeviceEntity;
import kernel360.trackycore.core.infrastructure.exception.CarException;
import kernel360.trackycore.core.infrastructure.exception.DeviceException;
import kernel360.trackyweb.car.application.mapper.CarMapper;
import kernel360.trackyweb.car.infrastructure.repository.CarRepository;
import kernel360.trackyweb.car.infrastructure.repository.DeviceRepository;
import kernel360.trackyweb.car.presentation.dto.CarCreateRequest;
import kernel360.trackyweb.car.presentation.dto.CarDetailResponse;
import kernel360.trackyweb.car.presentation.dto.CarResponse;
import kernel360.trackyweb.car.presentation.dto.CarUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

	private final CarRepository carRepository;
	private final DeviceRepository deviceRepository;

	/**
	 * 등록 차량 전체 조회
	 * @return 전체 차량 List
	 */
	@Transactional
	public ApiResponse<List<CarResponse>> getAll() {
		return ApiResponse.success(CarResponse.fromList(carRepository.findAll()));
	}

	/**
	 * mdn이 일치하는 차량 찾기
	 * @param mdn 차량 mdn
	 * @return 차량 단건 조회
	 */
	@Transactional
	public ApiResponse<Boolean> isMdnExist(String mdn) {
		return ApiResponse.success(carRepository.existsByMdn(mdn));
	}

	/**
	 * 필터링 기반 검색
	 * @param mdn
	 * @param status
	 * @param purpose
	 * @return 검색된 차량 List
	 */
	@Transactional
	public ApiResponse<List<CarResponse>> searchByFilter(String mdn, String status, String purpose) {
		List<CarEntity> cars = carRepository.searchByFilter(mdn, status, purpose);
		return ApiResponse.success(CarResponse.fromList(cars));
	}

	/**
	 * 차량 MDN 값으로 단건 검색
	 * @param mdn
	 * @return 단건 차량 데이터
	 */
	@Transactional
	public ApiResponse<CarResponse> searchOneByMdn(String mdn) {
		CarEntity car = carRepository.findByMdn(mdn)
			.orElseThrow(() -> CarException.notFound());
		return ApiResponse.success(CarResponse.from(car));
	}

	/**
	 * 디바이스 정보를 포함한 차량 MDN 값으로 검색
	 * @param mdn 차량 MDN
	 * @return 단건 차량 데이터 + 디바이스 정보
	 */
	@Transactional
	public ApiResponse<CarDetailResponse> searchOneDetailByMdn(String mdn) {
		CarEntity car = carRepository.findDetailByMdn(mdn)
			// 천승준 - 공통 에러 처리 해봤어요
			.orElseThrow(() -> CarException.notFound());
		return ApiResponse.success(CarDetailResponse.from(car));
	}

	/**
	 * 차량 신규 등록 ( device는 기본 device 설정 MDN 1 가져옴 )
	 * @param carCreateRequest
	 * @return 등록 성공한 차량 detail
	 */
	@Transactional
	public ApiResponse<CarDetailResponse> create(CarCreateRequest carCreateRequest) {
		DeviceEntity device = deviceRepository.findById(1L)
			.orElseThrow(() -> DeviceException.notFound());

		// device 세팅 넣은 car 객체 <- 임시로 모든 차량은 device 세팅 1번
		CarEntity car = CarMapper.createCar(carCreateRequest, device);

		if (carRepository.existsByMdn(carCreateRequest.mdn())) {
			throw CarException.duplicated();
		}
		CarEntity savedCar = carRepository.save(car);

		CarDetailResponse response = CarDetailResponse.from(savedCar);
		return ApiResponse.success(response);
	}

	/**
	 * 차량 정보 수정
	 * @param mdn 차량 mdn
	 * @param carUpdateRequest 차량 정보
	 * @return 수정된 차량 detail
	 */
	@Transactional
	public ApiResponse<CarDetailResponse> update(String mdn, CarUpdateRequest carUpdateRequest) {
		CarEntity car = carRepository.findDetailByMdn(mdn)
			.orElseThrow(() -> CarException.notFound());

		// 항상 MDN 1인 디바이스 사용
		DeviceEntity device = deviceRepository.findById(1L)
			.orElseThrow(() -> DeviceException.notFound());

		// update 할 객체 생성
		CarMapper.updateCar(car, carUpdateRequest, device);

		log.info("업데이트 차량 : {}", car);

		CarEntity updatedCar = carRepository.save(car);
		return ApiResponse.success(CarDetailResponse.from(updatedCar));
	}

	/**
	 * 차량 삭제 API
	 * @param mdn
	 * @return ApiResponse
	 */
	@Transactional
	public ApiResponse<String> delete(String mdn) {
		carRepository.deleteByMdn(mdn);
		return ApiResponse.success("삭제 완료");
	}
}
