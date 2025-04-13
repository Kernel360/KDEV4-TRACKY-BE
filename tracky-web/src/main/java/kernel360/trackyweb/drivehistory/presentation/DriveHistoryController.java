package kernel360.trackyweb.drivehistory.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.drivehistory.application.DriveHistoryService;
import kernel360.trackyweb.drivehistory.domain.CarDriveHistory;
import kernel360.trackyweb.drivehistory.domain.DriveHistory;
import kernel360.trackyweb.drivehistory.domain.RentDriveHistroyWithDriveList;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drives")
public class DriveHistoryController implements DriveHistoryApiDocs {

	private final DriveHistoryService driveHistoryService;

	@GetMapping("/history/rents")
	public ApiResponse<List<RentDriveHistroyWithDriveList>> findAllRentHistories(
		@RequestParam(required = false) String rentUuid
	) {
		List<RentDriveHistroyWithDriveList> result = driveHistoryService.getAllRentHistories(rentUuid);
		return ApiResponse.success(result);
	}

	@GetMapping("/history/{id}")
	public ApiResponse<DriveHistory> getDriveHistories(@PathVariable Long id) {
		DriveHistory history = driveHistoryService.getDriveHistoriesByDriveId(id);
		return ApiResponse.success(history);
	}

	@GetMapping("/history/cars")
	public ApiResponse<List<CarDriveHistory>> getDriveHistoriesByCar(
		@RequestParam(required = true) String mdn
	) {
		List<CarDriveHistory> histories = driveHistoryService.getDriveHistoriesByCar(mdn);
		return ApiResponse.success(histories);
	}
}
