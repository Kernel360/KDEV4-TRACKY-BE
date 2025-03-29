package kernel360.trackyweb.car.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel360.trackycore.core.common.ApiResponse;
import kernel360.trackyweb.car.application.CarService;
import kernel360.trackyweb.car.application.dto.CarDetailResponse;
import kernel360.trackyweb.car.application.dto.CarResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
@Tag(name = "Car API", description = "차량에 관련한 기본 CRUD API 입니다")
public class CarController {

	private final CarService carService;

	@GetMapping("all")
	@Operation(summary = "전체 차량 조회", description = "DB에 있는 모든 차량 리스트 조회")
	public ApiResponse<List<CarResponse>> getAll() {
		return carService.getAll();
	}

	@GetMapping("searchbymdn/{keyword}")
	@Operation(summary = "전체 차량 조회", description = "DB에 있는 모든 차량 리스트 조회")
	public ApiResponse<List<CarResponse>> searchByMdn(
		@PathVariable String keyword
 	) {
		return carService.searchByMdn(keyword);
	}

	@GetMapping("searchbyid/{id}")
	@Operation(summary = "단건 차량 조회", description = "DB에 있는 차량 중 id로 상세 조회")
	public ApiResponse<CarResponse> searchById(
		@PathVariable Long id
	) {
		return carService.searchById(id);
	}

	@GetMapping("searchbyid/detail/{id}")
	@Operation(summary = "단건 차량 조회", description = "DB에 있는 차량 중 id로 상세 조회, device 정보 포함")
	public ApiResponse<CarDetailResponse> searchDetailById(
		@PathVariable Long id
	) {
		return carService.searchDetailById(id);
	}

}
