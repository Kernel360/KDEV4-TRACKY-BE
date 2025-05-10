package kernel360.trackyweb.admin.statistic.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import kernel360.trackyweb.admin.statistic.application.dto.AdminBizListResponse;
import kernel360.trackyweb.admin.statistic.application.dto.AdminBizStatisticResponse;
import kernel360.trackyweb.admin.statistic.infrastructure.AdminStatisticRepositoryCustom;
import kernel360.trackyweb.statistic.domain.dto.MonthlyStat;
import kernel360.trackyweb.statistic.infrastructure.repository.MonthlyStatisticDomainRepository;
import kernel360.trackyweb.timedistance.infrastructure.repository.TimeDistanceDomainRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminStatisticProvider {

	private final AdminStatisticRepositoryCustom adminStatisticRepository;
	private final TimeDistanceDomainRepository timeDistanceDomainRepository;
	private final MonthlyStatisticDomainRepository monthlyStatisticDomainRepository;

	public List<AdminBizListResponse> getAdminBizList(LocalDate selectedDate) {
		return adminStatisticRepository.fetchAdminBizList(selectedDate);
	}

	public AdminBizStatisticResponse getDriveStatByBizIdAndDate(Long bizId, LocalDate selectedDate) {

		AdminBizStatisticResponse response = timeDistanceDomainRepository.getDriveStatByBizIdAndDate(bizId,
			selectedDate);
		List<MonthlyStat> monthlyStats = monthlyStatisticDomainRepository.getMonthlyStats(bizId, selectedDate,
			selectedDate.minusMonths(12));

		return response.update(monthlyStats);
	}
}
