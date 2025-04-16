package kernel360.trackyconsumer.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel360.trackyconsumer.application.dto.CarOnOffRequest;
import kernel360.trackyconsumer.application.dto.CycleGpsRequest;
import kernel360.trackyconsumer.application.dto.GpsHistoryMessage;
import kernel360.trackyconsumer.domain.provider.DriveDomainProvider;
import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.DriveEntity;
import kernel360.trackycore.core.common.entity.GpsHistoryEntity;
import kernel360.trackycore.core.common.entity.LocationEntity;
import kernel360.trackycore.core.common.entity.RentEntity;
import kernel360.trackycore.core.common.provider.CarProvider;
import kernel360.trackycore.core.common.provider.GpsHistoryProvider;
import kernel360.trackycore.core.common.provider.LocationProvider;
import kernel360.trackycore.core.common.provider.RentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

	private final DriveDomainProvider driveProvider;
	private final CarProvider carProvider;
	private final LocationProvider locationProvider;
	private final GpsHistoryProvider gpsHistoryProvider;
	private final RentProvider rentProvider;

	@Async("taskExecutor")
	@Transactional
	public void receiveCycleInfo(GpsHistoryMessage request) {

		// 처리 로직
		List<CycleGpsRequest> cycleGpsRequestList = request.getCList();

		CarEntity car = carProvider.findByMdn(request.getMdn());

		DriveEntity drive = driveProvider.findByCarAndOtime(car, request.getOTime());

		// GPS 쪼개서 정보 저장
		for (int i = 0; i < request.getCCnt(); i++) {
			saveCycleInfo(drive, request.getOTime(), cycleGpsRequestList.get(i).getGpsInfo().getSum(),
				cycleGpsRequestList.get(i));
		}
	}

	private void saveCycleInfo(DriveEntity drive, LocalDateTime oTime, double sum, CycleGpsRequest cycleGpsRequest) {

		GpsHistoryEntity gpsHistoryEntity = cycleGpsRequest.toGpsHistoryEntity(drive, oTime, sum);

		gpsHistoryProvider.save((gpsHistoryEntity));

		log.info("GpsHistory 저장 완료");
	}

	@Transactional
	public void processOnMessage(CarOnOffRequest carOnOffRequest) {

		LocationEntity location = LocationEntity.create(carOnOffRequest.getGpsInfo().getLon(),
			carOnOffRequest.getGpsInfo().getLat());

		locationProvider.save(location);

		CarEntity car = carProvider.findByMdn(carOnOffRequest.getMdn());

		RentEntity rent = rentProvider.findByCarAndTime(car, carOnOffRequest.getOnTime());

		DriveEntity drive = DriveEntity.create(car, rent, location,
			carOnOffRequest.getOnTime());

		driveProvider.save(drive);
	}

	@Transactional
	public void processOffMessage(CarOnOffRequest carOnOffRequest) {

		/** 시동 off시 다음 동작 수행
		 * 주행 종료 시간 update(Drive)
		 * 주행 별 거리 sum으로 update(Drive)
		 * 차량 주행 거리 += sum update(Car)
		 * 주행 종료 지점 update(Location)
		 **/
		CarEntity car = carProvider.findByMdn(carOnOffRequest.getMdn());

		DriveEntity drive = driveProvider.findByCarAndOtime(car, carOnOffRequest.getOnTime());
		drive.updateDistance(carOnOffRequest.getGpsInfo().getSum());
		drive.updateOffTime(carOnOffRequest.getOffTime());

		car.updateSum(drive.getDriveDistance());

		LocationEntity location = drive.getLocation();
		location.updateEndLocation(carOnOffRequest.getGpsInfo().getLat(), carOnOffRequest.getGpsInfo().getLon());
	}
}
