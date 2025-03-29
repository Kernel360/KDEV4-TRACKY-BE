package kernel360.trackyemulator.application.service.util;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

	private final double EARTH_RADIUS = 6371e3; // 지구 반지름 (미터)

	//두 위치간 거리 계산
	public int calculateDistance(int lat1, int lon1, int lat2, int lon2) {
		double latRad1 = Math.toRadians(lat1);
		double latRad2 = Math.toRadians(lat2);
		double deltaLat = Math.toRadians(lat2 - lat1);
		double deltaLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
			Math.cos(latRad1) * Math.cos(latRad2) *
				Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (int)(EARTH_RADIUS * c);
	}

	//방향(방위각, Bearing) 계산 (0~360도)
	public int calculateBearing(int lat1, int lon1, int lat2, int lon2) {
		double latRad1 = Math.toRadians(lat1);
		double latRad2 = Math.toRadians(lat2);
		double deltaLon = Math.toRadians(lon2 - lon1);

		double y = Math.sin(deltaLon) * Math.cos(latRad2);
		double x = Math.cos(latRad1) * Math.sin(latRad2) -
			Math.sin(latRad1) * Math.cos(latRad2) * Math.cos(deltaLon);

		double bearing = Math.toDegrees(Math.atan2(y, x));
		return (int)(bearing + 360) % 360; // 0~360도로 보정
	}

	//속도 계산 (m/s) → km/h로 변환
	public int calculateSpeed(int distanceMeters, int timeSeconds) {
		double speedMps = distanceMeters / timeSeconds;
		return (int)(speedMps * 3.6); // km/h
	}

}
