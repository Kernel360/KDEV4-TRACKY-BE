package kernel360trackybe.trackyhub.presentation;

import kernel360trackybe.trackyhub.application.dto.CycleInfoRequest;
import kernel360trackybe.trackyhub.application.dto.ApiResponse;
import kernel360trackybe.trackyhub.application.dto.CarOnOffRequest;
import kernel360trackybe.trackyhub.application.service.CarInfoProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/api/car", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class CarInfoController {

	private final CarInfoProducerService producerService;

	@PostMapping(value = "/cycle")
	public ApiResponse<String> sendCycleInfo(@RequestBody CycleInfoRequest cycleInfoRequest) {

		producerService.sendCycleInfo(cycleInfoRequest);
        return new ApiResponse<>("000", "Success", cycleInfoRequest.getMdn());
	}

	@PostMapping(value = "/on")
	public ApiResponse<String> sendCarStart(@RequestBody CarOnOffRequest carOnOffRequest) {

		producerService.sendCarStart(carOnOffRequest);
		return new ApiResponse<>("000", "Success", carOnOffRequest.getMdn());
	}

	// @PostMapping(value = "/off")
	// public ApiResponse<String> sendCarStop(@RequestBody CarOnOffRequest carOnOffRequest) {
	//
	// 	producerService.sendCarStop(carOnOffRequest);
	// 	return new ApiResponse<>("000", "Success", carOnOffRequest.getMdn());
	// }
}