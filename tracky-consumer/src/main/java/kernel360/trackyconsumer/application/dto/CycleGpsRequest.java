package kernel360.trackyconsumer.application.dto;

import java.time.LocalDateTime;

import kernel360.trackycore.core.infrastructure.entity.DriveEntity;
import kernel360.trackycore.core.infrastructure.entity.GpsHistoryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CycleGpsRequest {
	private int sec;    // 발생시간 '초'
	private String gcd;    // GPS 상태
	private long lat;    // GPS 위도
	private long lon;    // GPS 경도
	private int ang;    // 방향
	private int spd;    // 속도
	private int sum;    // 누적주행 거리

	public GpsHistoryEntity toGpsHistoryEntity(DriveEntity drive, LocalDateTime oTime, int sum) {
		return new GpsHistoryEntity(drive, oTime, this.gcd, this.lat, this.lon, this.ang, this.spd, sum);
	}
}