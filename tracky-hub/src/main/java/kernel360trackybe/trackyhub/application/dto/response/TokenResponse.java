package kernel360trackybe.trackyhub.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
	private String mdn;
	private String token;
	private String exPeriod;
}
