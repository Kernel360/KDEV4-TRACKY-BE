package kernel360trackybe.trackyhub.application.service;

import kernel360.trackycore.core.infrastructure.entity.DriveEntity;
import kernel360.trackycore.core.infrastructure.entity.GpsHistoryEntity;
import kernel360.trackycore.core.infrastructure.repository.DriveRepository;
import kernel360.trackycore.core.infrastructure.repository.GpsHistoryRepository;
import kernel360trackybe.trackyhub.config.RabbitMQConfig;
import kernel360trackybe.trackyhub.application.dto.GpsHistoryMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoConsumerService {

	private final DriveRepository driveRepository;
	private final GpsHistoryRepository gpsHistoryRepository;

	// GPS 정보 처리 큐
	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void receiveCarMessage(GpsHistoryMessage message) {

		DriveEntity drive = driveRepository.findByMdn(message.getMdn());
		GpsHistoryEntity gpsHistoryEntity = message.toGpsHistory(drive);

		gpsHistoryRepository.save(gpsHistoryEntity);
	}
}