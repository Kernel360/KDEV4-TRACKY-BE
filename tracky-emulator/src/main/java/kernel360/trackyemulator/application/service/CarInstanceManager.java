package kernel360.trackyemulator.application.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Component;

import kernel360.trackyemulator.application.service.CarInstanceFactory.MultiCarInstanceFactory;
import kernel360.trackyemulator.application.service.client.MdnListRequestClient;
import kernel360.trackyemulator.infrastructure.dto.ApiResponse;
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
	private final CycleDataManager cycleDataManager;
	private final MdnListRequestClient mdnListRequestClient;
	private final MultiCarInstanceFactory multiCarInstanceFactory;

	private List<EmulatorInstance> instances = new ArrayList<>();

	private List<String> mdnList;

	//현재 생성 가능한 에뮬레이터 개수 받아옴
	public int getAvailableEmulatorCount(){
		mdnList = mdnListRequestClient.getMdnList();

		return mdnList.size();
	}

	// 뷰에서 입력받은 에뮬레이터 개수만큼 인스턴스 생성
	public int createEmulator(int count) {
		if (count == 1) {
			log.info("카 인스턴스 매니저가 single car instance factory로 1개 인스턴스 생성 요청");
			this.instances = singleCarInstanceFactory.createCarInstances();
			log.info("single car instance factory가 {}개의 인스턴스 생성 완료", instances.size());
		} else {
			this.instances = multiCarInstanceFactory.createCarInstances(count, mdnList);
		}
		return instances.size();
	}

	//에뮬레이터에 토큰 세팅
	public Map<String, String> fetchAllTokens() {
		Map<String, String> result = new LinkedHashMap<>(); // 순서 유지

		for (EmulatorInstance instance : instances) {
			String token = tokenRequestClient.getToken(instance);
			instance.setToken(token);

			result.put(instance.getMdn(), "토큰 세팅 성공" + instance.getToken());
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

			cycleDataManager.startSending(instance); // 스케줄 시작
		}

		return result;
	}

	//시동 OFF 데이터 전송
	public Map<String, String> sendStopRequests() {
		log.info("시동 off manager 들어옴");
		Map<String, String> result = new LinkedHashMap<>();
		Set<String> stoppedMdnSet = new HashSet<>();

		for (EmulatorInstance instance : instances) {
			instance.setCarOffTime(LocalDateTime.now());

			ApiResponse response = stopRequestClient.sendCarStop(instance);
			log.info("시동 off response : {}", response.getRstMsg());

			result.put(instance.getMdn(), response.getRstMsg());

			cycleDataManager.stopSending(instance); // 스케줄 종료 + 남은 데이터 전송
			stoppedMdnSet.add(response.getMdn());
		}

		removeStoppedInstances(stoppedMdnSet); // 리스트에서 삭제

		return result;
	}

	//에뮬레이터 삭제
	private void removeStoppedInstances(Set<String> stoppedMdns) {
		instances.removeIf(instance -> stoppedMdns.contains(instance.getMdn()));
		stoppedMdns.forEach(mdn -> log.info("{} 인스턴스 삭제 완료", mdn));
	}

}
