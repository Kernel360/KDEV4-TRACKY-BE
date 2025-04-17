package kernel360trackybe.trackyhub.application.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;
import kernel360.trackycore.core.common.entity.vo.GpsInfo;

public record CarOnOffRequest(
	String mdn,
	String gcd,
	EmulatorInfo emulatorInfo,
	GpsInfo gpsInfo,
	LocalDateTime onTime,
	LocalDateTime offTime
) {
	@JsonCreator
	public CarOnOffRequest(
		@JsonProperty String mdn,
		@JsonProperty String gcd,
		@JsonProperty String tid,
		@JsonProperty String mid,
		@JsonProperty String did,
		@JsonProperty String pv,
		@JsonProperty int lat,
		@JsonProperty int lon,
		@JsonProperty int ang,
		@JsonProperty int spd,
		@JsonProperty double sum,
		@JsonProperty @JsonFormat(pattern = "yyyyMMddHHmm") LocalDateTime onTime,
		@JsonProperty @JsonFormat(pattern = "yyyyMMddHHmm") LocalDateTime offTime
	) {
		this(mdn, gcd, EmulatorInfo.create(tid, mid, did, pv), GpsInfo.create(lat, lon, ang, spd, sum), onTime,
			offTime);
	}
}
