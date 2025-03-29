package kernel360.trackyemulator.application.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import kernel360.trackyemulator.application.dto.ApiResponse;
import kernel360.trackyemulator.application.service.CarInstanceFactory.SingleCarInstanceFactory;
import kernel360.trackyemulator.application.service.client.StartRequestClient;
import kernel360.trackyemulator.application.service.client.StopRequestClient;
import kernel360.trackyemulator.application.service.client.TokenRequestClient;
import kernel360.trackyemulator.domain.EmulatorInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarInstanceManager {

	private final SingleCarInstanceFactory singleCarInstanceFactory;
	private final TokenRequestClient tokenRequestClient;
	private final StartRequestClient startRequestClient;
	private final StopRequestClient stopRequestClient;

	private List<EmulatorInstance> instances = new ArrayList<>();

	// 뷰에서 입력받은 에뮬레이터 개수만큼 인스턴스 생성
	public int createEmulator(int count) {
		if (count == 1) {
			log.info("카 인스턴스 매니저가 single car instance factory로 1개 인스턴스 생성 요청");
			this.instances = (List<EmulatorInstance>)singleCarInstanceFactory.createCarInstances();
			log.info("single car instance factory가 {}개의 인스턴스 생성 완료", instances.size());
		} else {
			throw new UnsupportedOperationException("지금은 1대만 지원합니다.");
		}
		return instances.size();
	}

	//에뮬레이터에 토큰 세팅
	public Map<String, String> fetchAllTokens() {
		Map<String, String> result = new LinkedHashMap<>(); // 순서 유지

		for (EmulatorInstance instance : instances) {
			String token = tokenRequestClient.getToken(instance);
			instance.setToken(token);

			result.put(instance.getMdn(), "토큰 세팅 성공");
			log.info("{} 토큰 세팅 완료", instance.getMdn());
		}
		return result;
	}

	//시동 ON 데이터 전송
	public Map<String, String> sendStartRequests() {
		Map<String, String> result = new LinkedHashMap<>();
		for (EmulatorInstance instance : instances) {
			ApiResponse response = startRequestClient.sendCarStart(instance);

			result.put(instance.getMdn(), response.getRstMsg());
		}

		return result;
	}

	//시동 OFF 데이터 전송
	public Map<String, String> sendStopRequests() {
		Map<String, String> result = new LinkedHashMap<>();
		for (EmulatorInstance instance : instances) {
			ApiResponse response = stopRequestClient.sendCarStop(instance);

			result.put(instance.getMdn(), response.getRstMsg());

			//시동 끈 에뮬레이터 인스턴스 삭제
			removeInstance(response.getMdn());
		}
		return result;
	}

	//TODO : 주기 정보 데이터 전송 (60초 주기)

	//시동 끈 에뮬레이터 인스턴스 (리스트에서)삭제
	private void removeInstance(String mdn) {
		instances.removeIf(instance -> instance.getMdn().equals(mdn));
		log.info("{} 인스턴스 삭제 완료", mdn);
	}
}
