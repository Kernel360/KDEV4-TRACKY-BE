package kernel360trackybe.trackyhub.application.service;

import kernel360trackybe.trackyhub.config.RabbitMQConfig;
import kernel360trackybe.trackyhub.application.dto.CycleInfoRequest;
import kernel360trackybe.trackyhub.application.dto.DistanceDto;
import kernel360trackybe.trackyhub.application.dto.CarMessage;
import kernel360trackybe.trackyhub.application.dto.CycleGpsRequest;
import kernel360trackybe.trackyhub.application.dto.GPSDto;
import kernel360trackybe.trackyhub.application.mapper.GPSMapper;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoProducerService {

	private final RabbitTemplate rabbitTemplate;
	private final GPSMapper gpsMapper;

	/**
	 * GPS 정보 RabbitMQ로 전송 및 누적주행 거리 계산해서 RabbitMQ로 전송
	 */
	public void sendCarInfo(CycleInfoRequest carInfo) {
		List<CycleGpsRequest> cycleGpsRequestList = carInfo.getCList();

		// 요청별로 라우팅 키를 설정하여 순서 보장
		String routingKey = RabbitMQConfig.generateRoutingKey(carInfo.getMdn());

		log.info("Received JSON data: {}", carInfo.toString());
		// 주기 정보(60개) 쪼개서 RabbitMQ로 전송
		// 초간 누적이동 거리가 80m 이상일 경우 해당 구간 스킵
		int skipDistance = 0;
		for (int i = 0; i < carInfo.getCCnt(); i++) {
			log.info("before sendGPS");
			sendGPS(carInfo, cycleGpsRequestList.get(i), routingKey);
			log.info("after sendGPS");
			// 누적주행 거리 계산(초간 이동거리 80m 이상인 경우 skip)
			if (i < carInfo.getCCnt() - 1)
				skipDistance -= calDistance(cycleGpsRequestList.get(i + 1).getSum(),
					cycleGpsRequestList.get(i).getSum());
		}
		// 모든 GPS 처리 후 누적 주행 거리 전송
		int finalDistance = cycleGpsRequestList.get(cycleGpsRequestList.size() - 1).getSum() - cycleGpsRequestList.get(0).getSum() - skipDistance;

		sendDistance(carInfo.getMdn(), finalDistance, routingKey);
	}

	// 초간 이동거리 계산(80m 이상인 경우 skip)
	public int calDistance(int next, int now) {
		int skipDistance = next - now;
		if (skipDistance >= 80) {
			return skipDistance;
		}
		return 0;
	}

	public void sendGPS(CycleInfoRequest carInfo, CycleGpsRequest cycleGpsRequest, String routingKey) {
		GPSDto gpsDto = gpsMapper.toGPSDto(carInfo, cycleGpsRequest);
		log.info("GPS 전송 시간(초): {}", gpsDto.getSec());

		// GPS 메시지 RabbitMQ로 전송
		CarMessage gpsMessage = CarMessage.builder()
			.messageType("GPS")
			.gpsData(gpsDto)
			.build();

		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,
			routingKey,
			gpsMessage
		);
	}

	public void sendDistance(String mdn, int finalDistance, String routingKey) {

		DistanceDto distanceDto = DistanceDto.builder()
			.mdn(mdn)
			.distance(finalDistance)
			.build();

		CarMessage distanceMessage = CarMessage.builder()
			.messageType("DISTANCE")
			.distanceData(distanceDto)
			.build();

		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,
			routingKey,
			distanceMessage
		);
	}
}