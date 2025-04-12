package kernel360.trackyweb.drivehistory.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kernel360.trackycore.core.common.entity.GpsHistoryEntity;

public record GpsData(int lat, int lon, int spd, LocalDateTime o_time) {
	public static GpsData create(int lat, int lon, int spd, LocalDateTime o_time) {
		return new GpsData(lat, lon, spd, o_time);
	}

	public static List<GpsData> createList(
		List<GpsHistoryEntity> gpsList
	) {
		return gpsList.stream().map(
				gps -> GpsData.create(gps.getLat(), gps.getLon(), gps.getSpd(), gps.getOTime()))
			.collect(Collectors.toList());
	}
}
