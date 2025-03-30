package kernel360.trackyemulator.application.service.client;

import kernel360.trackyemulator.presentation.dto.ApiResponse;
import kernel360.trackyemulator.presentation.dto.CycleInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CycleRequestClient {

    private final RestTemplate restTemplate;

    public ApiResponse sendCycleData(CycleInfoRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CycleInfoRequest> entity = new HttpEntity<>(request, headers);

        // Cycle 데이터 전송 API 호출
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "http://localhost:8082/api/car/cycle",
                entity,
                ApiResponse.class
        );

        // API 응답 처리
        ApiResponse apiResponse = response.getBody();
        if (apiResponse == null || !("000".equals(apiResponse.getRstCd()))) {
            throw new IllegalStateException(
                    "주기 데이터 전송 실패 " + request.getMdn());
        }

        log.info("{} → 60초 주기 데이터 전송 완료", request.getMdn());

        return apiResponse;
    }
}