package kernel360trackybe.trackyhub.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;

public record CycleInfoRequest(String mdn, EmulatorInfo emulatorInfo, int cCnt, LocalDateTime oTime,
							   List<CycleGpsRequest> cList) {

	@JsonCreator
	public CycleInfoRequest(
		@JsonProperty String mdn,
		@JsonProperty String tid,
		@JsonProperty String mid,
		@JsonProperty String did,
		@JsonProperty String pv,
		@JsonProperty int cCnt,
		@JsonProperty @JsonFormat(pattern = "yyyyMMddHHmm") LocalDateTime oTime,
		@JsonProperty List<CycleGpsRequest> cList
	) {
		this(mdn, EmulatorInfo.create(tid, mid, did, pv), cCnt, oTime, cList);
	}
}