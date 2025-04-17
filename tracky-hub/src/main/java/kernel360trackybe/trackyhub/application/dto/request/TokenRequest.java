package kernel360trackybe.trackyhub.application.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TokenRequest {
	private String mdn;
	@JsonUnwrapped
	private EmulatorInfo emulatorInfo;
	private String dFWVer;
}
