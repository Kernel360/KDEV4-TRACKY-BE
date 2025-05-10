package kernel360.trackyweb.admin.statistic.infrastructure;

import java.time.LocalDate;
import java.util.List;

import kernel360.trackyweb.admin.statistic.application.dto.AdminBizListResponse;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizStatisticResponse;

public interface AdminStatisticRepositoryCustom {

	List<AdminBizListResponse> fetchAdminBizList(LocalDate selectedDate);

	AdminBizStatisticResponse getDriveStatByBizIdAndDate(Long bizId, LocalDate selectedDate);
}
