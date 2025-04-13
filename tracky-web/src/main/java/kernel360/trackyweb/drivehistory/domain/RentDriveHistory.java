package kernel360.trackyweb.drivehistory.domain;

import java.time.LocalDateTime;

public record RentDriveHistory(
	String rentUuid,
	String renterName,
	String mdn,
	LocalDateTime rentStime,
	LocalDateTime rentEtime
) {
	public static RentDriveHistory create(String rentUuid, String renterName, String mdn,
		LocalDateTime rentStime, LocalDateTime rentEtime) {
		return new RentDriveHistory(rentUuid, renterName, mdn, rentStime, rentEtime); // ← 기본 생성자
	}
}
