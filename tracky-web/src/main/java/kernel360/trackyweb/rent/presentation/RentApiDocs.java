package kernel360.trackyweb.rent.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.rent.presentation.dto.RentRequest;
import kernel360.trackyweb.rent.presentation.dto.RentResponse;

@Tag(name = "Rent API", description = "렌트 관련 API")
public interface RentApiDocs {

	@Operation(summary = "대여 목록 전체 조회", description = "DB에 있는 모든 대여 리스트 조회")
	ApiResponse<List<RentResponse>> getAll();

	@Operation(summary = "rent uuid, status, date 필터링 검색", description = "rent uuid, status, date 필터링 검색")
	ApiResponse<List<RentResponse>> searchByFilter(
		@RequestParam String rentUuid,
		@RequestParam(required = false, name = "status") String rentStatus,
		@RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentDate // yyyy-MM-dd
	);

	@Operation(summary = "대여 신규 등록", description = "대여 신규 등록 API")
	ApiResponse<RentResponse> create(@RequestBody RentRequest rentRequest);

	@Operation(summary = "대여 UUID로 단건 조회", description = "rent_uuid로 단일 대여 조회 API")
	ApiResponse<RentResponse> searchDetailByRentUuid(@PathVariable String rentUuid);

	@Operation(summary = "대여 삭제", description = "대여 정보 삭제 API")
	ApiResponse<String> delete(@PathVariable String rentUuid);
}
