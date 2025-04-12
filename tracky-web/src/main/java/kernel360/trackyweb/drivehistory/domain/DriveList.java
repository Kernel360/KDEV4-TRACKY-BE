package kernel360.trackyweb.drivehistory.domain;

import java.time.LocalDateTime;

public record DriveList(
	Long driveId,
	int onLat,
	int onLon,
	int offLat,
	int offLon,
	double sum,
	LocalDateTime driveOnTime,
	LocalDateTime driveOffTime
) {
}
