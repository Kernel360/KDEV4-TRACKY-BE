package kernel360.trackyweb.drivehistory.domain;

import kernel360.trackycore.core.common.entity.GpsHistoryEntity;

public record GpsPoint(int lat, int lon) {

	public static GpsPoint from(GpsHistoryEntity gps) {
		if (gps == null) {
			return new GpsPoint(0, 0);
		}
		return new GpsPoint(gps.getLat(), gps.getLon());
	}

	public static GpsPoint noPoint() {
		return new GpsPoint(0, 0);
	}
}

