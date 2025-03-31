package kernel360trackybe.trackyhub.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
	private String rstCd;	// 결과 코드
	private String rstMsg;	// 결과 메시지
	private String mdn;		// 차량 번호
}
