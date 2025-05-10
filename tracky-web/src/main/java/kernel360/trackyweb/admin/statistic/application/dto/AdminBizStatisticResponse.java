package kernel360.trackyweb.admin.statistic.application.dto;

import java.util.List;

import kernel360.trackyweb.statistic.domain.dto.MonthlyStat;

public record AdminBizStatisticResponse(
	int dailyDriveCount,
	double dailyDriveDistance,
	int dailyDriveSec,
	List<MonthlyStat> monthlyStat
) {

	public AdminBizStatisticResponse update(List<MonthlyStat> monthlyStat) {
		return new AdminBizStatisticResponse(
			this.dailyDriveCount,
			this.dailyDriveDistance,
			this.dailyDriveSec,
			monthlyStat
		);
	}
}
