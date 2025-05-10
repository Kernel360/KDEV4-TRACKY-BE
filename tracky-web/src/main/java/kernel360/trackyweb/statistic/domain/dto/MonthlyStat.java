package kernel360.trackyweb.statistic.domain.dto;

public record MonthlyStat(
	int year,
	int month,
	int driveCount,
	double driveDistance
) {
}
