package kernel360.trackyemulator.application.service.client;

import java.time.LocalDateTime;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import kernel360.trackyemulator.application.dto.ApiResponse;
import kernel360.trackyemulator.application.dto.CarOnOffRequest;
import kernel360.trackyemulator.application.service.util.RandomLocationGenerator;
import kernel360.trackyemulator.domain.EmulatorInstance;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartRequestClient {

	private final RestTemplate restTemplate;
	private final RandomLocationGenerator locationGenerator;

	public ApiResponse sendCarStart(EmulatorInstance car) {
		//CarOnOffRequest DTO set
		CarOnOffRequest request = new CarOnOffRequest();
		request.setMdn(car.getMdn());
		request.setTid(car.getTid());
		request.setMid(car.getMid());
		request.setPv(car.getPv());
		request.setDid(car.getDid());

		request.setOnTime(LocalDateTime.now());
		request.setGcd("A");
		request.setLat(locationGenerator.randomLatitude());
		request.setLon(locationGenerator.randomLongitude());
		request.setAng(0);
		request.setSpd(0);
		request.setSum(0);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<CarOnOffRequest> entity = new HttpEntity<>(request, headers);

		//sendCarStart
		ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
			"/http://localhost:8082/api/car/",
			entity,
			ApiResponse.class
		);

		//응답
		ApiResponse apiResponse = response.getBody();
		if (apiResponse == null || !("000".equals(apiResponse.getRstCd()))) {
			throw new IllegalStateException(
				"Start 정보 전송 실패 " + apiResponse.getMdn());
		}

		return apiResponse;
	}
}
