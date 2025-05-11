package kernel360.trackyweb.admin.statistic.application;

import java.time.LocalDate;
import java.util.List;

import kernel360.trackyweb.admin.statistic.application.dto.response.GraphsResponse;
import kernel360.trackyweb.admin.statistic.application.dto.response.HourlyGraphResponse;
import kernel360.trackyweb.admin.statistic.application.dto.response.MonthlyDriveCountResponse;
import kernel360.trackyweb.statistic.domain.provider.DailyStatisticProvider;
import kernel360.trackyweb.statistic.domain.provider.MonthlyStatisticProvider;
import kernel360.trackyweb.timedistance.domain.provider.TimeDistanceDomainProvider;
import org.springframework.stereotype.Service;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackycore.core.domain.entity.BizEntity;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizListResponse;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizStatisticResponse;
import kernel360.trackyweb.admin.statistic.application.dto.AdminStatisticRequest;
import kernel360.trackyweb.admin.statistic.domain.AdminStatisticProvider;
import kernel360.trackyweb.biz.domain.provider.BizDomainProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminStatisticService {

	private final AdminStatisticProvider adminStatisticProvider;
	private final BizDomainProvider bizDomainProvider;
	private final TimeDistanceDomainProvider timeDistanceDomainProvider;
	private final DailyStatisticProvider dailyStatisticProvider;
	private final MonthlyStatisticProvider monthlyStatisticProvider;

	public ApiResponse<List<AdminBizListResponse>> getAdminBizList(LocalDate selectedDate) {

		List<AdminBizListResponse> bizList = adminStatisticProvider.getAdminBizList(selectedDate);

		return ApiResponse.success(bizList);
	}

	public ApiResponse<AdminBizStatisticResponse> getAdminBizStatistics(
		AdminStatisticRequest adminStatisticRequest) {

		Long bizId = null;
		if (!adminStatisticRequest.bizName().isBlank()) {
			BizEntity biz = bizDomainProvider.getBizByBizName(adminStatisticRequest.bizName());
			bizId = biz.getId();
		}

		AdminBizStatisticResponse bizStat = adminStatisticProvider.getDriveStatByBizIdAndDate(
			bizId, adminStatisticRequest.selectedDate());

		return ApiResponse.success(bizStat);
	}

    public ApiResponse<List<HourlyGraphResponse>> getHourlyGraph() {
		return ApiResponse.success(HourlyGraphResponse.toResultList(timeDistanceDomainProvider.getYesterdayData()));
    }

	public ApiResponse<GraphsResponse> getGraphs() {
		List<GraphsResponse.CarCount> carCountWithBizName = dailyStatisticProvider.getCarCountWithBizName();
		List<GraphsResponse.OperationRate> operationRateWithBizName = dailyStatisticProvider.getOperationRatesAvgWithBizName();
		List<GraphsResponse.NonOperatedCar> nonOperatedCarWithBizName = monthlyStatisticProvider.getNonOperatedCarWithBizName();
		List<GraphsResponse.DriveCount> monthlyDriveCount = monthlyStatisticProvider.getDriveCount();

		return ApiResponse.success(GraphsResponse.toResponse(
				carCountWithBizName, operationRateWithBizName, nonOperatedCarWithBizName, monthlyDriveCount));
	}

	public ApiResponse<List<MonthlyDriveCountResponse>> getAdminBizMonthlyDriveCount(String bizName) {
		return ApiResponse.success(MonthlyDriveCountResponse.toDto(monthlyStatisticProvider.getAdminBizMonthlyDriveCount(bizName)));
	}
}
