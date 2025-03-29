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
import kernel360.trackyemulator.application.service.util.DistanceCalculator;
import kernel360.trackyemulator.application.service.util.RandomLocationGenerator;
import kernel360.trackyemulator.domain.EmulatorInstance;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StopRequestClient {

	private final RestTemplate restTemplate;

	public ApiResponse sendCarStop(EmulatorInstance car) {
		//CarOnOffRequest DTO set
		CarOnOffRequest request = new CarOnOffRequest();
		request.setMdn(car.getMdn());
		request.setTid(car.getTid());
		request.setMid(car.getMid());
		request.setPv(car.getPv());
		request.setDid(car.getDid());

		request.setOffTime(LocalDateTime.now());
		request.setGcd("A");
		request.setLat(car.getCycleLastLat());
		request.setLon(car.getCycleLastLon());
		request.setAng(car.getCycleLastAng());
		request.setSpd(car.getCycleLastSpeed());
		request.setSum(car.getSum());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<CarOnOffRequest> entity = new HttpEntity<>(request, headers);

		//sendCarStart
		ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
			"/http://localhost:8082/api/car/시동 오프",
			entity,
			ApiResponse.class
		);

		//응답
		ApiResponse apiResponse = response.getBody();
		if (apiResponse == null || !("000".equals(apiResponse.getRstCd()))) {
			throw new IllegalStateException(
				"Stop 정보 전송 실패 " + apiResponse.getMdn());
		}

		return apiResponse;
	}
}
