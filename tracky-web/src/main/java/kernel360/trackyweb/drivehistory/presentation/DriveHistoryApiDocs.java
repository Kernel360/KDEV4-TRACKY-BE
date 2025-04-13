package kernel360.trackyweb.drivehistory.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.drivehistory.domain.CarDriveHistory;
import kernel360.trackyweb.drivehistory.domain.DriveHistory;
import kernel360.trackyweb.drivehistory.domain.RentDriveHistroyWithDriveList;

@Tag(name = "DriveHistory Api", description = "운행 기록 API")
public interface DriveHistoryApiDocs {

	@Operation(summary = "예약자 조회", description = "예약자 관련 운행 정보 조회")
	ApiResponse<List<RentDriveHistroyWithDriveList>> findAllRentHistories(
		@RequestParam(required = false) String rentUuid
	);

	@Operation(summary = "단일 주행 상세 기록 조회", description = "주행 기록 단건 조회")
	ApiResponse<DriveHistory> getDriveHistories(@PathVariable Long id);

	@Operation(summary = "차량 기준 주행 기록 조회", description = "차량 별 주행 기록 조회")
	ApiResponse<List<CarDriveHistory>> getDriveHistoriesByCar(
		@RequestParam(required = true) String mdn
	);
}


