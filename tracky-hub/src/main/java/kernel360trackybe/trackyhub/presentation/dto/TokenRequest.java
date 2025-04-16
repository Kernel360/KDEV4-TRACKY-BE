package kernel360trackybe.trackyhub.presentation.dto;

import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TokenRequest {

	private EmulatorInfo emulatorInfo;
	private String dFWVer;
}
