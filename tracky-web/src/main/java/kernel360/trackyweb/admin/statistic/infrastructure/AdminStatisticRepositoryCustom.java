package kernel360.trackyweb.admin.statistic.infrastructure;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.Tuple;

import kernel360.trackyweb.admin.statistic.application.dto.AdminBizListResponse;

public interface AdminStatisticRepositoryCustom {

	List<AdminBizListResponse> fetchAdminBizList(LocalDate selectedDate);

	List<Tuple> findTotalCarCountAndBizName();

	List<Tuple> countRunningDriveAndSkipCount(LocalDate selectedDate);

}
