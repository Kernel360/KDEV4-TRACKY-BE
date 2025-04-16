package kernel360trackybe.trackyhub.presentation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel360trackybe.trackyhub.application.service.CarInfoProducerService;
import kernel360trackybe.trackyhub.presentation.dto.ApiResponse;
import kernel360trackybe.trackyhub.presentation.dto.ApiTokenResponse;
import kernel360trackybe.trackyhub.presentation.dto.CarOnOffRequest;
import kernel360trackybe.trackyhub.presentation.dto.CycleInfoRequest;
import kernel360trackybe.trackyhub.presentation.dto.TokenRequest;
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
		return new ApiResponse("000", "Success", cycleInfoRequest.getMdn());
	}

	@PostMapping(value = "/on")
	public ApiResponse sendCarStart(@RequestBody CarOnOffRequest carOnOffRequest) {

		producerService.sendCarStart(carOnOffRequest);
		return new ApiResponse("000", "Success", carOnOffRequest.getMdn());
	}

	@PostMapping(value = "/off")
	public ApiResponse sendCarStop(@RequestBody CarOnOffRequest carOnOffRequest) {

		producerService.sendCarStop(carOnOffRequest);
		return new ApiResponse("000", "Success", carOnOffRequest.getMdn());
	}

	@PostMapping(value = "/token")
	public ApiTokenResponse getToken(@RequestBody TokenRequest tokenRequest) {

		String token = producerService.getToken();
		return new ApiTokenResponse("000", "Success", tokenRequest.getMdn(), token, "4");
	}
}