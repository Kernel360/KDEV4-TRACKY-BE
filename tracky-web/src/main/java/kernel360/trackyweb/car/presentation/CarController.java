package kernel360.trackyweb.car.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.car.application.CarService;
import kernel360.trackyweb.car.presentation.dto.CarCreateRequest;
import kernel360.trackyweb.car.presentation.dto.CarDetailResponse;
import kernel360.trackyweb.car.presentation.dto.CarResponse;
import kernel360.trackyweb.car.presentation.dto.CarUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController implements CarApiDocs {

	private final CarService carService;

	@GetMapping("/all")
	public ApiResponse<List<CarResponse>> getAll() {
		return carService.getAll();
	}

	@GetMapping("/check-mdn/{mdn}")
	public ApiResponse<Boolean> isMdnExist(
		@PathVariable String mdn
	) {
		return carService.isMdnExist(mdn);
	}

	@GetMapping("/search")
	public ApiResponse<List<CarResponse>> searchByFilter(
		@RequestParam(required = false) String mdn,
		@RequestParam(required = false) String status,
		@RequestParam(required = false) String purpose
	) {
		return carService.searchByFilter(mdn, status, purpose);
	}

	@GetMapping("/search/{mdn}")
	public ApiResponse<CarResponse> searchOneByMdn(
		@PathVariable String mdn
	) {
		return carService.searchOneByMdn(mdn);
	}

	@GetMapping("/search/{mdn}/detail")
	public ApiResponse<CarDetailResponse> searchOneDetailByMdn(
		@PathVariable String mdn
	) {
		log.info("searchOneDetailByMdn : {}", mdn);
		return carService.searchOneDetailByMdn(mdn);
	}

	@PostMapping("/create")
	public ApiResponse<CarDetailResponse> create(
		@RequestBody CarCreateRequest carCreateRequest
	) {
		return carService.create(carCreateRequest);
	}

	@PatchMapping("/update/{mdn}")
	public ApiResponse<CarDetailResponse> update(
		@PathVariable String mdn,
		@RequestBody CarUpdateRequest carUpdateRequest
	) {
		return carService.update(mdn, carUpdateRequest);
	}

	@DeleteMapping("/delete/{mdn}")
	public ApiResponse<String> delete(
		@PathVariable String mdn
	) {
		return carService.delete(mdn);
	}
}
