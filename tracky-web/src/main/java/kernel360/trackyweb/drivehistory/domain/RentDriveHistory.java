package kernel360.trackyweb.drivehistory.domain;

import java.time.LocalDateTime;
import java.util.List;

public record RentDriveHistory(
	String rentUuid,
	String renterName,
	String mdn,
	LocalDateTime rentStime,
	LocalDateTime rentEtime,
	List<DriveList> drivelist
) {
	public static RentDriveHistory create(String rentUuid, String renterName, String mdn,
		LocalDateTime rentStime, LocalDateTime rentEtime, List<DriveList> drivelist) {
		return new RentDriveHistory(rentUuid, renterName, mdn, rentStime, rentEtime, drivelist); // ← 기본 생성자
	}
}
