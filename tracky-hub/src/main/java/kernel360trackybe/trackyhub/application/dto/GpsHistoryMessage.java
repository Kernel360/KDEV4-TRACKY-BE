package kernel360trackybe.trackyhub.application.dto;

import kernel360.trackycore.core.domain.entity.DriveEntity;
import kernel360.trackycore.core.domain.entity.GpsHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GpsHistoryMessage {
	private String mdn;
	private LocalDateTime oTime;  // 발생시간

	// Cycle specific data
	// private int sec;    // 발생시간 '초'
	private String gcd;    // GPS 상태
	private long lat;    // GPS 위도
	private long lon;    // GPS 경도
	private String ang;    // 방향
	private String spd;    // 속도
	private int sum;		// 단건 주행 거리

	// private GpsHistoryMessage(String mdn, String tid, String mid, String pv, String did,
	// 	LocalDateTime oTime, int sec, String gcd, long lat, long lon,
	// 	String ang, String spd) {
	// 	this.mdn = mdn;
	// 	this.tid = tid;
	// 	this.mid = mid;
	// 	this.pv = pv;
	// 	this.did = did;
	// 	this.oTime = oTime;
	// 	this.sec = sec;
	// 	this.gcd = gcd;
	// 	this.lat = lat;
	// 	this.lon = lon;
	// 	this.ang = ang;
	// 	this.spd = spd;
	// }

	public static GpsHistoryMessage from(String mdn, LocalDateTime otime, CycleGpsRequest cycleGpsRequest, int sum) {
		return new GpsHistoryMessage(
			mdn,
			otime,
			cycleGpsRequest.getGcd(),
			cycleGpsRequest.getLat(),
			cycleGpsRequest.getLon(),
			cycleGpsRequest.getAng(),
			cycleGpsRequest.getSpd(),
			sum
		);
	}

	public GpsHistoryEntity toGpsHistory(DriveEntity drive) {

		return new GpsHistoryEntity(
			drive,
			this.oTime,
			this.gcd,
			this.lat,
			this.lon,
			this.ang,
			this.spd,
			this.sum
		);
	}
}