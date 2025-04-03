package kernel360trackybe.trackyhub.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CycleGpsRequest {
	private int sec;    // 발생시간 '초'
	private String gcd;    // GPS 상태
	private int lat;    // GPS 위도
	private int lon;    // GPS 경도
	private int ang;    // 방향
	private int spd;    // 속도
	private double sum;    // 누적주행 거리
}