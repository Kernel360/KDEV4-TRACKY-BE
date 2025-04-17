package kernel360trackybe.trackyhub.application.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;
import kernel360.trackycore.core.common.entity.vo.GpsInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarOnOffRequest {
	private String mdn;
	private String gcd;
	@JsonUnwrapped
	private EmulatorInfo emulatorInfo;
	@JsonUnwrapped
	private GpsInfo gpsInfo;

	@JsonFormat(pattern = "yyyyMMddHHmm")
	private LocalDateTime onTime;  // 시동 On 시간

	@JsonFormat(pattern = "yyyyMMddHHmm")
	private LocalDateTime offTime; // 시동 Off 시간
}
