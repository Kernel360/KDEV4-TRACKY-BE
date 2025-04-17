package kernel360trackybe.trackyhub.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record GpsHistoryMessage(String mdn, LocalDateTime oTime, int cCnt,
								List<CycleGpsRequest> cList) {

	public GpsHistoryMessage(String mdn, @JsonFormat(pattern = "yyyyMMddHHmm") LocalDateTime oTime, int cCnt,
		List<CycleGpsRequest> cList) {
		this.mdn = mdn;
		this.oTime = oTime;
		this.cCnt = cCnt;
		this.cList = cList;
	}

	public static GpsHistoryMessage from(String mdn, LocalDateTime otime, int cCnt, List<CycleGpsRequest> cList) {
		return new GpsHistoryMessage(mdn, otime, cCnt, cList);
	}
}