package kernel360.trackyweb.admin.statistic.application;

import java.time.LocalDate;
import java.util.List;

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
}
