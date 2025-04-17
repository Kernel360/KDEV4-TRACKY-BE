package kernel360trackybe.trackyhub.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kernel360.trackycore.core.common.entity.vo.GpsInfo;

public record CycleGpsRequest(int sec, String gcd, GpsInfo gpsInfo) {

	@JsonCreator
	public CycleGpsRequest(
		@JsonProperty int sec,
		@JsonProperty String gcd,
		@JsonProperty int lat,
		@JsonProperty int lon,
		@JsonProperty int ang,
		@JsonProperty int spd,
		@JsonProperty double sum
	) {
		this(sec, gcd, GpsInfo.create(lat, lon, ang, spd, sum));
	}
}