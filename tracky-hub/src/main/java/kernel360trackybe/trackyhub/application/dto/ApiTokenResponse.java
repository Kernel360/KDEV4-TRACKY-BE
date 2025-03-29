package kernel360trackybe.trackyhub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ApiTokenResponse {
	private String rstCd;
	private String rstMsg;
	private String mdn;
	private String token;
	private String exPeriod;
}
