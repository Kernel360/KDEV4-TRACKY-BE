package kernel360.trackyweb.car.presentation;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.car.application.dto.request.CarCreateRequest;
import kernel360.trackyweb.car.application.dto.request.CarUpdateRequest;
import kernel360.trackyweb.car.application.dto.response.CarDetailResponse;
import kernel360.trackyweb.car.application.dto.response.CarResponse;

@Tag(name = "Car API", description = "차량 관련 API")
public interface CarApiDocs {

	@Operation(summary = "mdn 중복 체크", description = "mdn 중복 체크")
	ApiResponse<Boolean> existsByMdn(@PathVariable String mdn);

	@Operation(summary = "mdn, status, purpose 필터링 검색", description = "mdn, status, purpose 필터링 차량 검색합니다")
	ApiResponse<List<CarResponse>> getAllBySearchFilter(@RequestParam String text,
		@RequestParam(required = false) String status,
		Pageable pageable);

	@Operation(summary = "차량 MDN으로 상세 조회", description = "MDN 기준으로 차량 및 디바이스 상세 정보를 조회합니다.")
	ApiResponse<CarDetailResponse> searchOne(@PathVariable String mdn);

	@Operation(summary = "차량 신규 등록", description = "차량 신규 등록 API")
	ApiResponse<CarDetailResponse> create(@RequestBody CarCreateRequest carCreateRequest);

	@Operation(summary = "차량 정보 수정", description = "차량 정보를 수정하는 API")
	ApiResponse<CarDetailResponse> update(@PathVariable String mdn, @RequestBody CarUpdateRequest carUpdateRequest);

	@Operation(summary = "차량 삭제", description = "차량 정보 삭제 API")
	ApiResponse<String> delete(@PathVariable String mdn);
}
