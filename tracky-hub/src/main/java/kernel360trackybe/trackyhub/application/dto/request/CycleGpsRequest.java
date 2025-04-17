package kernel360trackybe.trackyhub.application.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import kernel360.trackycore.core.common.entity.vo.GpsInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CycleGpsRequest {
	private int sec;    // 발생시간 '초'
	private String gcd;    // GPS 상태
	@JsonUnwrapped
	private GpsInfo gpsInfo;
}