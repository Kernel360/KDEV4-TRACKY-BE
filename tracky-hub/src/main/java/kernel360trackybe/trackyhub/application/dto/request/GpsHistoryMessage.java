package kernel360trackybe.trackyhub.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GpsHistoryMessage {
	private String mdn;
	private LocalDateTime oTime;  // 발생시간
	private int cCnt;

	private List<CycleGpsRequest> cList;

	public static GpsHistoryMessage from(String mdn, LocalDateTime otime, int cCnt, List<CycleGpsRequest> cList) {
		return new GpsHistoryMessage(
			mdn,
			otime,
			cCnt,
			cList
		);
	}
}