package kernel360trackybe.trackyhub.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

import lombok.Builder;

@Data
@Builder
public class GPSDto {
	private String mdn;    // 차량 번호
	private String tid;    // 터미널 아이디
	private String mid;    // 제조사 아이디
	private String pv;     // 패킷 버전
	private String did;    // 디바이스 아이디
	private LocalDateTime oTime;  // 발생시간

	// Cycle specific data
	private int sec;    // 발생시간 '초'
	private String gcd;    // GPS 상태
	private long lat;    // GPS 위도
	private long lon;    // GPS 경도
	private String ang;    // 방향
	private String spd;    // 속도
	private String bat;    // 배터리 전압

	public GPSDto convertToGPSDto(CycleInfoRequest carInfo, CycleGpsRequest cycleGpsRequest) {
		return GPSDto.builder()
			.mdn(carInfo.getMdn())
			.tid(carInfo.getTid())
			.mid(carInfo.getMid())
			.pv(carInfo.getPv())
			.did(carInfo.getDid())
			.oTime(carInfo.getOTime())
			.sec(cycleGpsRequest.getSec())
			.gcd(cycleGpsRequest.getGcd())
			.lat(cycleGpsRequest.getLat())
			.lon(cycleGpsRequest.getLon())
			.ang(cycleGpsRequest.getAng())
			.spd(cycleGpsRequest.getSpd())
			.build();
	}
}