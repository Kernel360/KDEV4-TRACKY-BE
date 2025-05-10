package kernel360.trackyweb.admin.statistic.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.admin.statistic.application.AdminStatisticService;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizListResponse;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizStatisticResponse;
import kernel360.trackyweb.admin.statistic.application.dto.AdminStatisticRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/statistic")
@RequiredArgsConstructor
public class AdminStatisticController {

	private final AdminStatisticService adminStatisticService;

	@GetMapping("/biz")
	public ApiResponse<List<AdminBizListResponse>> getAdminBizList(
		@RequestParam
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate
	) {
		return adminStatisticService.getAdminBizList(selectedDate);
	}

	@GetMapping("/biz/stat")
	public ApiResponse<AdminBizStatisticResponse> getAdminBizStatistics(
		@ModelAttribute AdminStatisticRequest adminStatisticRequest) {
		return adminStatisticService.getAdminBizStatistics(adminStatisticRequest);
	}

	@GetMapping("/monthly")
	public ApiResponse<AdminBizStatisticResponse> getMonthlyDriveCounts(AdminStatisticRequest adminStatisticRequest) {
		return null;
	}

	@GetMapping("/daily")
	public ApiResponse<AdminBizStatisticResponse> getDailyDriveCounts(AdminStatisticRequest adminStatisticRequest) {
		return null;
	}

	@GetMapping("/hourly")
	public ApiResponse<AdminBizStatisticResponse> getHourlyDriveCounts(AdminStatisticRequest adminStatisticRequest) {
		return null;
	}

	@GetMapping("/graphs")
	public ApiResponse<AdminBizStatisticResponse> getGraphs() {
		return null;
	}
}
