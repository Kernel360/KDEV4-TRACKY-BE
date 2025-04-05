package kernel360.trackyweb.rent.presentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
import kernel360.trackyweb.rent.presentation.dto.RentRequest;
import kernel360.trackyweb.rent.presentation.dto.RentResponse;
import kernel360.trackyweb.rent.application.service.RentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
// @PreAuthorize("hasRole('admin')")
@RequestMapping("/api/rents")
@RequiredArgsConstructor
public class RentController implements RentApiDocs {

	private final RentService rentService;

	@GetMapping("/all")
	public ApiResponse<List<RentResponse>> getAll() {
		return rentService.getAll();
	}


	@GetMapping("/search")
	public ApiResponse<List<RentResponse>> searchByFilter(
		@RequestParam(required = false) String rentUuid,
		@RequestParam(required = false, name = "status") String rentStatus,
		@RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentDate // yyyy-MM-dd
	) {
		// LocalDate를 LocalDateTime으로 변환
		LocalDateTime rentDateTime = rentDate != null ? rentDate.atStartOfDay() : null;
		return rentService.searchByFilter(rentUuid, rentStatus, rentDateTime);
	}

	@GetMapping("/search/{rentUuid}/detail")
	public ApiResponse<RentResponse> searchDetailByRentUuid(
		@PathVariable String rentUuid
	){
		log.info("searchDetailByRentUuid : {}" , rentUuid);
		return rentService.searchDetailByRentUuid(rentUuid);
	}

	@PostMapping("/create")
	public ApiResponse<RentResponse> create(
		@RequestBody RentRequest rentRequest
	){
		return rentService.create(rentRequest);
	}

	@PatchMapping("/update/{rentUuid}")
	public ApiResponse<RentResponse> update(
		@PathVariable String rentUuid,
		@RequestBody RentRequest rentRequest
	) {
		return rentService.update(rentUuid, rentRequest);
	}

	@DeleteMapping("/delete/{rentUuid}")
	public ApiResponse<String> delete(
		@PathVariable String rentUuid
	) {
		return rentService.delete(rentUuid);
	}

}