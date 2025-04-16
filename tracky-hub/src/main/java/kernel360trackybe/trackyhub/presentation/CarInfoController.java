package kernel360trackybe.trackyhub.presentation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel360trackybe.trackyhub.application.dto.request.CarOnOffRequest;
import kernel360trackybe.trackyhub.application.dto.request.CycleInfoRequest;
import kernel360trackybe.trackyhub.application.dto.request.TokenRequest;
import kernel360trackybe.trackyhub.application.dto.response.ApiResponse;
import kernel360trackybe.trackyhub.application.dto.response.TokenResponse;
import kernel360trackybe.trackyhub.application.service.CarInfoProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/hub/car", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class CarInfoController {

	private final CarInfoProducerService producerService;

	@PostMapping(value = "/cycle")
	public ApiResponse sendCycleInfo(@RequestBody CycleInfoRequest cycleInfoRequest) {

		producerService.sendCycleInfo(cycleInfoRequest);
		return ApiResponse.success(cycleInfoRequest.getMdn());
	}

	@PostMapping(value = "/on")
	public ApiResponse sendCarStart(@RequestBody CarOnOffRequest carOnOffRequest) {

		producerService.sendCarStart(carOnOffRequest);
		return ApiResponse.success(carOnOffRequest.getMdn());
	}

	@PostMapping(value = "/off")
	public ApiResponse<String> sendCarStop(@RequestBody CarOnOffRequest carOnOffRequest) {

		producerService.sendCarStop(carOnOffRequest);
		return ApiResponse.success(carOnOffRequest.getMdn());
	}

	@PostMapping(value = "/token")
	public ApiResponse<TokenResponse> getToken(@RequestBody TokenRequest tokenRequest) {

		String token = producerService.getToken();
		TokenResponse tokenResponse = new TokenResponse(tokenRequest.getMdn(), token, "4");
		return ApiResponse.success(tokenResponse);
	}
}