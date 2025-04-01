package kernel360trackybe.trackyhub.application.service;

import kernel360trackybe.trackyhub.presentation.dto.CarOnOffRequest;
import kernel360trackybe.trackyhub.config.RabbitMQConfig;
import kernel360trackybe.trackyhub.presentation.dto.CycleInfoRequest;
import kernel360trackybe.trackyhub.presentation.dto.GpsHistoryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoProducerService {

	private final RabbitTemplate rabbitTemplate;


	public void sendCarStart(CarOnOffRequest carOnOffRequest) {

		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,
			"on",
			carOnOffRequest
		);
	}

	public void sendCarStop(CarOnOffRequest carOnOffRequest) {

		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,
			"off",
			carOnOffRequest
		);
	}

	/**
	 * GPS 정보 RabbitMQ로 전송 및 누적주행 거리 계산해서 RabbitMQ로 전송
	 */
	public void sendCycleInfo(CycleInfoRequest carInfo) {

		GpsHistoryMessage gpsHistoryMessage = GpsHistoryMessage.from(carInfo.getMdn(), carInfo.getOTime(), carInfo.getCCnt(), carInfo.getCList());

		log.info("GPS 전송:{}" , gpsHistoryMessage.toString());
		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,
			"gps",
			gpsHistoryMessage
		);
	}

    public String getToken() {
        return UUID.randomUUID().toString();
    }
}