package kernel360.trackyconsumer.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel360.trackyconsumer.application.dto.request.CarOnOffRequest;
import kernel360.trackyconsumer.application.dto.request.CycleGpsRequest;
import kernel360.trackyconsumer.application.dto.request.GpsHistoryMessage;
import kernel360.trackyconsumer.domain.provider.DriveDomainProvider;
import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.DriveEntity;
import kernel360.trackycore.core.common.entity.GpsHistoryEntity;
import kernel360.trackycore.core.common.entity.LocationEntity;
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
		List<CycleGpsRequest> cycleGpsRequestList = request.cList();

		CarEntity car = carProvider.findByMdn(request.mdn());

		DriveEntity drive = driveProvider.findByCarAndOtime(car, request.oTime());

		// GPS 쪼개서 정보 저장
		for (int i = 0; i < request.cCnt(); i++) {
			saveCycleInfo(drive, request.oTime(), cycleGpsRequestList.get(i).gpsInfo().getSum(),
				cycleGpsRequestList.get(i));
		}
	}

	private void saveCycleInfo(DriveEntity drive, LocalDateTime oTime, double sum, CycleGpsRequest cycleGpsRequest) {

		GpsHistoryEntity gpsHistoryEntity = cycleGpsRequest.toGpsHistoryEntity(drive, oTime, sum);

		gpsHistoryProvider.save((gpsHistoryEntity));
	}

	@Transactional
	public void processOnMessage(CarOnOffRequest carOnOffRequest) {

		LocationEntity location = LocationEntity.create(carOnOffRequest.gpsInfo().getLon(),
			carOnOffRequest.gpsInfo().getLat());
		locationProvider.save(location);

		CarEntity car = carProvider.findByMdn(carOnOffRequest.mdn());

		DriveEntity drive = DriveEntity.create(car, location,
			carOnOffRequest.onTime());

		driveProvider.save(drive);
	}

	@Transactional
	public void processOffMessage(CarOnOffRequest carOnOffRequest) {

		CarEntity car = carProvider.findByMdn(carOnOffRequest.mdn());

		DriveEntity drive = driveProvider.findByCarAndOtime(car, carOnOffRequest.onTime());
		drive.off(carOnOffRequest.gpsInfo().getSum(), carOnOffRequest.offTime());

		car.updateDistance(drive.getDriveDistance());

		LocationEntity location = drive.getLocation();
		location.updateEndLocation(carOnOffRequest.gpsInfo().getLat(), carOnOffRequest.gpsInfo().getLon());
	}
}
