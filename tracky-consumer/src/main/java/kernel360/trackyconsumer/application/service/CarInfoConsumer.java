package kernel360.trackyconsumer.application.service;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.transaction.Transactional;
import kernel360.trackyconsumer.application.dto.CarOnOffRequest;
import kernel360.trackyconsumer.application.dto.CycleGpsRequest;
import kernel360.trackyconsumer.infrastructure.repository.CarEntityRepository;
import kernel360.trackyconsumer.infrastructure.repository.DeviceEntityRepository;
import kernel360.trackyconsumer.infrastructure.repository.DriveRepository;
import kernel360.trackyconsumer.infrastructure.repository.GpsHistoryRepository;
import kernel360.trackyconsumer.infrastructure.repository.LocationEntityRepository;
import kernel360.trackyconsumer.infrastructure.repository.RentEntityRepository;
import kernel360.trackycore.core.infrastructure.entity.CarEntity;
import kernel360.trackycore.core.infrastructure.entity.DeviceEntity;
import kernel360.trackycore.core.infrastructure.entity.DriveEntity;
import kernel360.trackycore.core.infrastructure.entity.GpsHistoryEntity;
import kernel360.trackyconsumer.config.RabbitMQConfig;
import kernel360.trackyconsumer.application.dto.GpsHistoryMessage;
import kernel360.trackycore.core.infrastructure.entity.LocationEntity;
import kernel360.trackycore.core.infrastructure.entity.RentEntity;
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
	private final LocationEntityRepository locationEntityRepository;
	private final RentEntityRepository rentEntityRepository;
	private final CarEntityRepository carEntityRepository;

	@RabbitListener(queues = RabbitMQConfig.ONOFF_QUEUE_NAME)
	@Transactional
	public void receiveCarOnOffMessage(@Payload CarOnOffRequest message, @Header("amqp_receivedRoutingKey") String routingKey) {

		log.info("시동 메시지 수신: {}", message.toString());

		switch (routingKey) {
			case "on":
				processOnMessage(message);
				break;
			case "off":
				processOffMessage(message);
				break;
		}
	}

	public void processOnMessage(CarOnOffRequest carOnOffRequest) {

		LocationEntity location = carOnOffRequest.toLocationEntity();
		locationEntityRepository.save(location);

		String mdn = carOnOffRequest.getMdn();
		CarEntity car = carEntityRepository.findByMdn(mdn);

		RentEntity rent = rentEntityRepository.findMyMdnAndTime(mdn, carOnOffRequest.getOnTime());

		DriveEntity drive = DriveEntity.create(mdn, rent.getId(), car.getDevice().getId(), location, carOnOffRequest.getOnTime());

		// 임시 사용
		// LocalDateTime tmp = LocalDateTime.now();
		// DriveEntity drive = DriveEntity.create(mdn, 1, 1, location, tmp);
		// 임시 사용

		driveRepository.save(drive);
	}

	public void processOffMessage(CarOnOffRequest carOnOffRequest) {

		/** 시동 off시 다음 동작 수행
		 * 주행 종료 시간 update(Drive)
		 * 주행 별 거리 sum으로 update(Drive)
		 * 차량 주행 거리 += sum update(Car)
		 * 주행 종료 지점 update(Location)
		 **/
		DriveEntity drive = driveRepository.findByMdnAndOtime(carOnOffRequest.getMdn(), carOnOffRequest.getOnTime());
		drive.updateDistance(carOnOffRequest.getSum());
		drive.updateOffTime(carOnOffRequest.getOffTime());

		CarEntity car = carEntityRepository.findByMdn(carOnOffRequest.getMdn());
		car.updateSum(drive.getDriveDistance());

		LocationEntity location = drive.getLocation();
		location.updateEndLocation(carOnOffRequest.getLat(), carOnOffRequest.getLon());
	}

	// GPS 정보 처리 큐
	@RabbitListener(queues = RabbitMQConfig.GPS_QUEUE_NAME)
	@Transactional
	public void receiveCarMessage(GpsHistoryMessage message) {

		log.info("요청 시간: {}", message.getOTime().toString());
		log.info("DB 시간: {}", driveRepository.findById(245L).get().getDriveOnTime());

		List<CycleGpsRequest> cycleGpsRequestList = message.getCList();
		// Drive 어떻게 찾지?

		DriveEntity drive = driveRepository.findByMdnAndOtime(message.getMdn(), message.getOTime());
		log.info("드라이브 엔티티: {}", drive.toString());

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
		log.info("히스토리 엔티티: {}", gpsHistoryEntity.toString());
		
		gpsHistoryRepository.save(gpsHistoryEntity);
	}


}