package kernel360.trackyemulator.application.service.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import kernel360.trackyemulator.application.service.dto.request.CarOnOffRequest;
import kernel360.trackyemulator.application.service.dto.response.ApiResponse;
import kernel360.trackyemulator.domain.EmulatorInstance;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartRequestClient {

	private final RestTemplate restTemplate;

	public ApiResponse sendCarStart(EmulatorInstance car) {

		//CarOnOffRequest DTO 생성
		CarOnOffRequest request = CarOnOffRequest.ofOn(car);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<CarOnOffRequest> entity = new HttpEntity<>(request, headers);

		//sendCarStart API 호출
		ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
			"http://hub-service.hub1:8082/hub/car/on",
			entity,
			ApiResponse.class
		);

		//API 응답
		ApiResponse apiResponse = response.getBody();
		if (apiResponse == null || !("000".equals(apiResponse.rstCd()))) {
			throw new IllegalStateException(
				"Start 정보 전송 실패 " + request.mdn());
		}

		return apiResponse;
	}
}
