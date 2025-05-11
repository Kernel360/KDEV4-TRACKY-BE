package kernel360.trackyweb.admin.statistic.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GraphsResponse (
        List<CarCount> carCountWithBizName,
        List<OperationRate> operationRateWithBizName,
        List<NonOperatedCar> nonOperatedCarWithBizName,
        List<DriveCount> monthlyDriveCount
) {
    public record CarCount(String bizName, int carCount) {}
    public record OperationRate(String bizName, double rate) {}
    public record NonOperatedCar(String bizName, int nonOperatedCarCount) {}
    public record DriveCount(LocalDate date, int driveCount) {}

    public static GraphsResponse toResponse(
            List<CarCount> carCount, List<OperationRate> operationRate,
            List<NonOperatedCar> nonOperatedCar, List<DriveCount> monthlyDriveCount) {
        return new GraphsResponse(carCount, operationRate, nonOperatedCar, monthlyDriveCount);
    }
}