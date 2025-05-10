package kernel360.trackyweb.admin.statistic.application.dto;

public record AdminBizListResponse(
	String bizName,
	int totalCarCount,
	int drivingCarCount,
	int skipCount
) {
}


/*
		for(int i=0; i<bizCarTuples.size(); i++) {
			Tuple bizCar = bizCarTuples.get(i);
			Tuple drive = driveTuples.get(i);
 */