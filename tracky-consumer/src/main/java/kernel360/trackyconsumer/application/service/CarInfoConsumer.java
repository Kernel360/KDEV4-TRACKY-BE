package kernel360.trackyconsumer.application.service;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.transaction.Transactional;
import kernel360.trackyconsumer.application.dto.CycleGpsRequest;
import kernel360.trackyconsumer.infrastructure.repository.CarEntityRepository;
import kernel360.trackyconsumer.infrastructure.repository.DriveRepository;
import kernel360.trackyconsumer.infrastructure.repository.GpsHistoryRepository;
import kernel360.trackyconsumer.infrastructure.repository.LocationEntityRepository;
import kernel360.trackyconsumer.infrastructure.repository.RentEntityRepository;
import kernel360.trackycore.core.infrastructure.entity.DriveEntity;
import kernel360.trackycore.core.infrastructure.entity.GpsHistoryEntity;
import kernel360.trackyconsumer.config.RabbitMQConfig;
import kernel360.trackyconsumer.application.dto.GpsHistoryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoConsumer {

	private final DriveRepository driveRepository;
	private final GpsHistoryRepository gpsHistoryRepository;
	// private final LocationEntityRepository locationEntityRepository;
	// private final CarEntityRepository carEntityRepository;
	// private final RentEntityRepository rentEntityRepository;

	// GPS 정보 처리 큐
	@RabbitListener(queues = RabbitMQConfig.GPS_QUEUE_NAME)
	@Transactional
	public void receiveCarMessage(GpsHistoryMessage message) {

		List<CycleGpsRequest> cycleGpsRequestList = message.getCList();
		DriveEntity drive = driveRepository.findByMdnAndOtime(message.getMdn(), message.getOTime());

		// GPS 쪼개서 정보 저장
		for (int i = 0; i < message.getCCnt(); i++) {

			if (i == 0) {
				saveGpsMessage(drive, message.getOTime(), cycleGpsRequestList.get(i).getSum(), cycleGpsRequestList.get(i));
			}
			else {
				int nowSum = cycleGpsRequestList.get(i).getSum();
				int beforeSum = cycleGpsRequestList.get(i - 1).getSum();
				int sum = nowSum - beforeSum;
				saveGpsMessage(drive, message.getOTime(), sum, cycleGpsRequestList.get(i));
			}
		}
	}

	public void saveGpsMessage(DriveEntity drive, LocalDateTime oTime, int sum, CycleGpsRequest cycleGpsRequest) {

		GpsHistoryEntity gpsHistoryEntity = cycleGpsRequest.toGpsHistoryEntity(drive, oTime, sum);
		
		gpsHistoryRepository.save(gpsHistoryEntity);
	}

	// @RabbitListener(queues = RabbitMQConfig.ONOFF_QUEUE_NAME)
	// @Transactional
	// public void receiveCarOnOffMessage(@Payload CarOnOffRequest message, @Header("amqp_receivedRoutingKey") String routingKey) {
	//
	// 	switch (routingKey) {
	// 		case "on":
	// 			processOnMessage(message);
	// 			break;
	// 		case "off":
	// 			break;
	// 	}
	// }
	//
	// public void processOnMessage(CarOnOffRequest carOnOffRequest) {
	//
	// 	LocationEntity location = carOnOffRequest.toLocationEntity();
	// 	locationEntityRepository.save(location);
	//
	// 	String mdn = carOnOffRequest.getMdn();
	// 	RentEntity rent = rentEntityRepository.findMyMdnAndTime(mdn, carOnOffRequest.getOnTime());
	//
	// 	// DriveEntity drive = DriveEntity.create(mdn, rent.getId(), car.getDevice().getId(), "1");
	// 	DriveEntity drive = DriveEntity.create(mdn, 1, 1, 1);
	// 	driveRepository.save(drive);
	// }
}