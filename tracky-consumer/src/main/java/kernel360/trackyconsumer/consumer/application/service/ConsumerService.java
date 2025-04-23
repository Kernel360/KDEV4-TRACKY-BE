package kernel360.trackyconsumer.consumer.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel360.trackyconsumer.consumer.application.dto.request.CarOnOffRequest;
import kernel360.trackyconsumer.consumer.application.dto.request.CycleGpsRequest;
import kernel360.trackyconsumer.consumer.application.dto.request.GpsHistoryMessage;
import kernel360.trackyconsumer.drive.domain.provider.DriveDomainProvider;
import kernel360.trackyconsumer.rent.domain.provider.RentDomainProvider;
import kernel360.trackycore.core.domain.entity.CarEntity;
import kernel360.trackycore.core.domain.entity.DriveEntity;
import kernel360.trackycore.core.domain.entity.GpsHistoryEntity;
import kernel360.trackycore.core.domain.entity.LocationEntity;
import kernel360.trackycore.core.domain.entity.RentEntity;
import kernel360.trackycore.core.domain.provider.CarProvider;
import kernel360.trackycore.core.domain.provider.GpsHistoryProvider;
import kernel360.trackycore.core.domain.provider.LocationProvider;
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
	private final RentDomainProvider rentDomainProvider;

	@Async("taskExecutor")
	@Transactional
	public void receiveCycleInfo(GpsHistoryMessage request) {

		List<CycleGpsRequest> cycleGpsRequestList = request.cList();

		CarEntity car = carProvider.findByMdn(request.mdn());

		DriveEntity drive = driveProvider.getDrive(car, request.oTime());

		List<GpsHistoryEntity> gpsHistoryList = toGpsHistoryList(cycleGpsRequestList, drive, request.oTime());

		gpsHistoryProvider.saveAll(gpsHistoryList);
	}

	@Transactional
	public void processOnMessage(CarOnOffRequest carOnOffRequest) {

		LocationEntity location = LocationEntity.create(carOnOffRequest.gpsInfo().getLon(),
			carOnOffRequest.gpsInfo().getLat());
		locationProvider.save(location);

		CarEntity car = carProvider.findByMdn(carOnOffRequest.mdn());

		RentEntity rent = rentDomainProvider.getRent(car, carOnOffRequest.onTime());

		DriveEntity drive = DriveEntity.create(car, rent, location,
			carOnOffRequest.onTime());

		driveProvider.save(drive);
	}

	@Transactional
	public void processOffMessage(CarOnOffRequest carOnOffRequest) {

		CarEntity car = carProvider.findByMdn(carOnOffRequest.mdn());

		DriveEntity drive = driveProvider.getDrive(car, carOnOffRequest.onTime());
		drive.off(carOnOffRequest.gpsInfo().getSum(), carOnOffRequest.offTime());

		car.updateDistance(drive.getDriveDistance());

		LocationEntity location = drive.getLocation();
		location.updateEndLocation(carOnOffRequest.gpsInfo().getLat(), carOnOffRequest.gpsInfo().getLon());
	}

	private List<GpsHistoryEntity> toGpsHistoryList(List<CycleGpsRequest> cycleGpsRequestList, DriveEntity drive,
		LocalDateTime oTime) {

		return cycleGpsRequestList.stream().map(request -> {
			return request.toGpsHistoryEntity(drive, oTime, request.gpsInfo().getSum());
		}).toList();
	}
}