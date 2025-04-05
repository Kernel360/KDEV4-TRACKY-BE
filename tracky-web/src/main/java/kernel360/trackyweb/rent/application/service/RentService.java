package kernel360.trackyweb.rent.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.RentEntity;
import kernel360.trackycore.core.infrastructure.exception.CarException;
import kernel360.trackycore.core.infrastructure.exception.RentException;
import kernel360.trackyweb.rent.application.mapper.RentMapper;
import kernel360.trackyweb.rent.presentation.dto.RentRequest;
import kernel360.trackyweb.rent.presentation.dto.RentResponse;
import kernel360.trackyweb.rent.infrastructure.repository.CarRepository;
import kernel360.trackyweb.rent.infrastructure.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentService {

	private final RentRepository rentRepository;
	@Qualifier("carRepositoryForRent") // 4월 1일 공통화 작업
	private final CarRepository carRepository;

	// 8자리 UUID 생성 메서드
	private String generateShortUuid() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}

	/**
	 * 렌트 정보 전체 조회
	 */
	public ApiResponse<List<RentResponse>> getAll() {
		return ApiResponse.success(rentRepository.findAll().stream()
			.map(RentResponse::from)
			.collect(Collectors.toList()));
	}

	/**
	 * 필터링 기반 검색
	 * @param rentUuid
	 * @param rentStatus
	 * @param rentDate
	 * @return 검색된 예약 List
	 */
	public ApiResponse<List<RentResponse>> searchByFilter(String rentUuid, String rentStatus, LocalDateTime rentDate) {
		List<RentEntity> results = rentRepository.searchByFilters(rentUuid, rentStatus, rentDate);
		return ApiResponse.success(RentResponse.fromList(results));
	}


	/**
	 * rentUuid 값으로 검색
	 * @param rentUuid
	 * @return 수정된 대여 detail
	 */
	public ApiResponse<RentResponse> searchDetailByRentUuid(String rentUuid) {
		RentEntity rent = rentRepository.findDetailByRentUuid(rentUuid)
			.orElseThrow(() -> RentException.notFound());
		return ApiResponse.success(RentResponse.from(rent));
	}



	/**
	 * 대여 신규 등록
	 * @param
	 * @return 등록 성공한 대여
	 */
	public ApiResponse<RentResponse> create(RentRequest rentRequest) {
		CarEntity car = carRepository.findByMdn(rentRequest.mdn())
			.orElseThrow(() -> CarException.notFound());

		// RentEntity 생성 (차량과 대여 정보 포함, uuid 만들기 (임시 8자리)
		String rentUuid = generateShortUuid();

		// 구지원 - 임시로 예약 등록은 전부 '대여 전'
		RentEntity rent = rentRequest.toEntity(rentUuid, "대여 전");

		RentEntity savedRent = rentRepository.save(rent);

		RentResponse response = RentResponse.from(savedRent);

		return ApiResponse.success(response);
	}

	/**
	 * 대여 정보 수정
	 * @param rentUuid
	 * @return 수정된 대여 detail
	 */
	@Transactional
	public ApiResponse<RentResponse> update(String rentUuid, RentRequest rentRequest) {
		RentEntity rent = rentRepository.findDetailByRentUuid(rentUuid)
			.orElseThrow(() -> RentException.notFound());

		RentMapper.updateRent(rent, rentRequest);

		log.info("업테이트 대여 : {}", rent);

		RentEntity updatedRent = rentRepository.save(rent);
		return ApiResponse.success(RentResponse.from(updatedRent));
	}



	/**
	 * 대여 삭제 API
	 * @param rentUuid
	 * @return ApiResponse
	 */
	@Transactional
	public ApiResponse<String> delete(String rentUuid) {
		rentRepository.deleteByRentUuid(rentUuid);
		return ApiResponse.success("삭제 완료");
	}
}