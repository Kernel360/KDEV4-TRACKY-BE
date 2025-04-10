package kernel360.trackyweb.drivehistory.domain;

import java.time.LocalDateTime;
import java.util.List;

public record DriveHistory(
	Long driveId,
	String rentUuid,
	String mdn,
	int onLat,
	int onLon,
	int offLat,
	int offLon,
	double sum,
	String renterName,
	String renterPhone,
	String rentStatus,
	String purpose,
	LocalDateTime driveOnTime,
	LocalDateTime driveOffTime,
	List<GpsData> gpsDataList
) {
	public static DriveHistory create(
		Long driveId,
		String rentUuid,
		String mdn,
		int onLat,
		int onLon,
		int offLat,
		int offLon,
		double sum,
		String renterName,
		String renterPhone,
		String rentStatus,
		String purpose,
		LocalDateTime driveOnTime,
		LocalDateTime driveOffTime,
		List<GpsData> gpsDataList
	) {
		return new DriveHistory(
			driveId,
			rentUuid,
			mdn,
			onLat,
			onLon,
			offLat,
			offLon,
			sum,
			renterName,
			renterPhone,
			rentStatus,
			purpose,
			driveOnTime,
			driveOffTime,
			gpsDataList
		);
	}
}
