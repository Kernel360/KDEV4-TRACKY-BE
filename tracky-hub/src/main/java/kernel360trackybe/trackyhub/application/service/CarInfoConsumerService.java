package kernel360trackybe.trackyhub.application.service;

import kernel360trackybe.trackyhub.config.RabbitMQConfig;
import kernel360trackybe.trackyhub.domain.Car;
import kernel360trackybe.trackyhub.domain.History;
import kernel360trackybe.trackyhub.application.dto.CarMessage;
import kernel360trackybe.trackyhub.application.dto.DistanceDto;
import kernel360trackybe.trackyhub.application.dto.GPSDto;
import kernel360trackybe.trackyhub.infrastructure.repository.CarRepository;
import kernel360trackybe.trackyhub.infrastructure.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoConsumerService {

	private final HistoryRepository historyRepository;
	private final CarRepository carRepository;

	// GPS 정보 처리 큐
	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void receiveCarMessage(CarMessage message) {
		if ("GPS".equals(message.getMessageType())) {
			GPSDto gpsData = message.getGpsData();
			History history = History.from(gpsData);
			log.info("History: {}", history);
			historyRepository.save(history);
		} else if ("DISTANCE".equals(message.getMessageType())) {
			DistanceDto distanceData = message.getDistanceData();
			log.info("Distance Data Received: {}", distanceData);
			Car car = carRepository.findByMdn(distanceData.getMdn())
				.orElseThrow(() -> new RuntimeException("차량 정보를 찾을 수 없습니다: " + distanceData.getMdn()));

			car.updateSum(distanceData.getDistance()); // 영속성 컨텍스트 내에서 처리 가능(save 안써도 됨)
			carRepository.save(car);
		}
	}
}