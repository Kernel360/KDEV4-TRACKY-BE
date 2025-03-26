package kernel360trackybe.trackyhub.application.dto;

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
	private String ang;    // 방향
	private String spd;    // 속도
	private int sum;    // 누적주행 거리
}