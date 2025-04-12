package kernel360.trackyweb.drivehistory.application.utils;

import kernel360.trackycore.core.common.entity.GpsHistoryEntity;
import kernel360.trackyweb.drivehistory.domain.GpsPoint;

public class GpsUtils {

	public static GpsPoint toPoint(GpsHistoryEntity gps) {
		if (gps == null) {
			return new GpsPoint(0, 0);
		}
		return new GpsPoint(gps.getLat(), gps.getLon());
	}
}
