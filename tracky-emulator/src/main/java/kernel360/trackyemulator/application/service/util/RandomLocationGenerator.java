package kernel360.trackyemulator.application.service.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 에뮬레이터 초기 위치 랜덤 생성기
 * 대한민국 전체 범위 내 위도/경도 생성
 */
public class RandomLocationGenerator {

	private static final double MIN_LAT = 35.0;
	private static final double MAX_LAT = 37.8;
	private static final double MIN_LON = 126.0;
	private static final double MAX_LON = 129.5;

	// 랜덤 위도 long 값 생성
	public static int randomLatitude() {
		double lat = ThreadLocalRandom.current().nextDouble(MIN_LAT, MAX_LAT);
		return (int)(lat * 1_000_000);
	}

	// 랜덤 경도 long 값 생성
	public static int randomLongitude() {
		double lon = ThreadLocalRandom.current().nextDouble(MIN_LON, MAX_LON);
		return (int)(lon * 1_000_000);
	}

	// 0~359도 사이 랜덤 방향 각도 생성
	public static int randomAngle() {
		return ThreadLocalRandom.current().nextInt(0, 360);
	}
}
