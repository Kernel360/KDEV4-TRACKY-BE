package kernel360trackybe.trackyhub.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TokenRequest {

	private String mdn;
	private String tid;
	private String mid;
	private String pv;
	private String did;
	private String dFWVer;
}
