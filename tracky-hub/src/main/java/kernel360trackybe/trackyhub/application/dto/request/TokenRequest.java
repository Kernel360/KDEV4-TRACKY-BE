package kernel360trackybe.trackyhub.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;

public record TokenRequest(String mdn, EmulatorInfo emulatorInfo, String dFWVer) {

	@JsonCreator
	public TokenRequest(
		@JsonProperty String mdn,
		@JsonProperty String tid,
		@JsonProperty String mid,
		@JsonProperty String did,
		@JsonProperty String pv,
		@JsonProperty String dFWVer
	) {
		this(mdn, EmulatorInfo.create(tid, mid, did, pv), dFWVer);
	}
}
