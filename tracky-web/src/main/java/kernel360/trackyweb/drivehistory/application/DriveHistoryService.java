package kernel360.trackyweb.drivehistory.application;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kernel360.trackycore.core.common.entity.DriveEntity;
import kernel360.trackycore.core.common.entity.GpsHistoryEntity;
import kernel360.trackycore.core.common.entity.RentEntity;
import kernel360.trackycore.core.infrastructure.exception.RentException;
import kernel360.trackyweb.drivehistory.domain.CarDriveHistory;
import kernel360.trackyweb.drivehistory.domain.DriveHistory;
import kernel360.trackyweb.drivehistory.domain.GpsData;
import kernel360.trackyweb.drivehistory.domain.RentDriveHistory;
import kernel360.trackyweb.drivehistory.infrastructure.repository.DriveHistoryRepository;
import kernel360.trackyweb.drivehistory.infrastructure.repository.GpsHistoryRepository;
import kernel360.trackyweb.drivehistory.infrastructure.repository.RentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriveHistoryService {

	private final RentHistoryRepository rentHistoryRepository;
	private final DriveHistoryRepository driveHistoryRepository;
	private final GpsHistoryRepository gpsHistoryRepository;

	public List<RentDriveHistory> getAllRentHistories(String rentUuid) {
		// 기본 렌트 정보 (rentUuid, renterName, mdn, rentStime, rentEtime) 조회
		List<RentDriveHistory> rents = rentHistoryRepository.findAllRentHistories().stream()
			.sorted(Comparator.comparing(RentDriveHistory::rentStime).reversed())
			.limit(10)
			.collect(Collectors.toList());

		if (rentUuid != null && !rentUuid.isEmpty()) {
			rents = rents.stream()
				.filter(r -> r.rentUuid().equals(rentUuid))
				.collect(Collectors.toList());
		}

		return rents.stream()
			.map(rentDto -> {
				// 각 렌트에 연결된 drive 리스트 조회
				List<DriveEntity> drives = driveHistoryRepository.findAllByRent_RentUuid(rentDto.rentUuid());

				// drive 리스트를 drivelistDto로 변환
				List<RentDriveHistory.DrivelistDto> driveList = drives.stream()
					.map(drive -> {
						Optional<GpsHistoryEntity> onGps = gpsHistoryRepository.findFirstGpsByDriveId(
							drive.getId());
						Optional<GpsHistoryEntity> offGps = gpsHistoryRepository.findLastGpsByDriveId(
							drive.getId());

						return new RentDriveHistory.DrivelistDto(
							drive.getId(),
							onGps.map(GpsHistoryEntity::getLat).orElse(0),
							onGps.map(GpsHistoryEntity::getLon).orElse(0),
							offGps.map(GpsHistoryEntity::getLat).orElse(0),
							offGps.map(GpsHistoryEntity::getLon).orElse(0),
							drive.getDriveDistance(),
							drive.getDriveOnTime(),
							drive.getDriveOffTime()
						);
					})
					.collect(Collectors.toList());

				// rent + drive 요약 리스트를 포함한 DTO 새로 생성해서 리턴
				return new RentDriveHistory(
					rentDto.rentUuid(),
					rentDto.renterName(),
					rentDto.mdn(),
					rentDto.rentStime(),
					rentDto.rentEtime(),
					driveList
				);
			})
			.collect(Collectors.toList());
	}

	public DriveHistory getDriveHistoriesByDriveId(Long id) {
		DriveEntity drive = driveHistoryRepository.findById(id)
			.orElseThrow(() -> RentException.notFound());

		Optional<GpsHistoryEntity> onGpsOpt = gpsHistoryRepository.findFirstGpsByDriveId(
			drive.getId());
		Optional<GpsHistoryEntity> offGpsOpt = gpsHistoryRepository.findLastGpsByDriveId(
			drive.getId());

		int onLat = onGpsOpt.map(GpsHistoryEntity::getLat).orElse(0);
		int onLon = onGpsOpt.map(GpsHistoryEntity::getLon).orElse(0);
		int offLat = offGpsOpt.map(GpsHistoryEntity::getLat).orElse(0);
		int offLon = offGpsOpt.map(GpsHistoryEntity::getLon).orElse(0);
		RentEntity rent = drive.getRent();

		List<GpsHistoryEntity> gpsList = gpsHistoryRepository.findByDriveId(drive.getId());
		List<GpsData> gpsDataList = gpsList.stream()
			.map(
				gps -> GpsData.create(gps.getLat(), gps.getLon(), gps.getSpd(), gps.getOTime()))
			.collect(Collectors.toList());

		return DriveHistory.create(
			drive.getId(),
			rent.getRentUuid(),
			drive.getCar().getMdn(),
			onLat, onLon,
			offLat, offLon,
			drive.getDriveDistance(),
			rent.getRenterName(),
			rent.getRenterPhone(),
			rent.getRentStatus(),
			rent.getPurpose(),
			drive.getDriveOnTime(),
			drive.getDriveOffTime(),
			gpsDataList
		);
	}

	/**
	 * 차량 mdn 검색으로 차량에 대한 운행정보 가져오기
	 * @param mdn
	 * @return 차량 별 운행기록 list
	 */
	public List<CarDriveHistory> getDriveHistoriesByCar(String mdn) {
		List<DriveEntity> drives = driveHistoryRepository.findAllByCar_Mdn(mdn);
		List<CarDriveHistory> results = drives.stream()
			.map(drive -> {
				Optional<GpsHistoryEntity> onGpsOpt = gpsHistoryRepository.findFirstGpsByDriveId(
					drive.getId());
				Optional<GpsHistoryEntity> offGpsOpt = gpsHistoryRepository.findLastGpsByDriveId(
					drive.getId());

				int onLat = onGpsOpt.map(GpsHistoryEntity::getLat).orElse(0);
				int onLon = onGpsOpt.map(GpsHistoryEntity::getLon).orElse(0);
				int offLat = offGpsOpt.map(GpsHistoryEntity::getLat).orElse(0);
				int offLon = offGpsOpt.map(GpsHistoryEntity::getLon).orElse(0);
				double sum = drive.getDriveDistance();
				RentEntity rent = drive.getRent();

				return CarDriveHistory.create(drive.getId(), rent.getRentUuid(), mdn, onLat, onLon, offLat,
					offLon, sum,
					drive.getDriveOnTime(),
					drive.getDriveOffTime());
			})
			.collect(Collectors.toList());

		return results;
	}

}
