package kernel360.trackyweb.drivehistory.domain;

import java.util.List;

public record RentDriveHistroyWithDriveList(
	RentDriveHistory rentDriveHistory,
	List<DriveList> driveList
) {
	public static RentDriveHistroyWithDriveList create(
		RentDriveHistory rentDriveHistory,
		List<DriveList> driveList
	) {
		return new RentDriveHistroyWithDriveList(
			rentDriveHistory, driveList
		);
	}
}

