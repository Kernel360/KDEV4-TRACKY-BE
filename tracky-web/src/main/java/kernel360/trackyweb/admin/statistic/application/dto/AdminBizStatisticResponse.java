package kernel360.trackyweb.admin.statistic.application.dto;

import java.util.List;

import kernel360.trackyweb.statistic.application.dto.response.MonthlyStatisticResponse;

public record AdminBizStatisticResponse(
	int dailyDriveCount,
	double dailyDriveDistance,
	int dailyDriveSec,
	List<MonthlyStatisticResponse.MonthlyStats> monthlyStat
) {

	public AdminBizStatisticResponse update(List<MonthlyStatisticResponse.MonthlyStats> monthlyStat) {
		return new AdminBizStatisticResponse(
			this.dailyDriveCount,
			this.dailyDriveDistance,
			this.dailyDriveSec,
			monthlyStat
		);
	}
}
