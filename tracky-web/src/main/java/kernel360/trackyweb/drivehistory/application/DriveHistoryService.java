package kernel360.trackyweb.drivehistory.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kernel360.trackycore.core.common.entity.DriveEntity;
import kernel360.trackycore.core.common.entity.GpsHistoryEntity;
import kernel360.trackycore.core.common.entity.RentEntity;
import kernel360.trackycore.core.infrastructure.exception.RentException;
import kernel360.trackyweb.drivehistory.domain.CarDriveHistory;
import kernel360.trackyweb.drivehistory.domain.DriveHistory;
import kernel360.trackyweb.drivehistory.domain.DriveList;
import kernel360.trackyweb.drivehistory.domain.GpsData;
import kernel360.trackyweb.drivehistory.domain.GpsPoint;
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

	// 최초 시작 gps point
	private GpsPoint getFirstGpsPoint(Long driveId) {
		return gpsHistoryRepository.findFirstGpsByDriveId(driveId)
			.map(GpsPoint::from)
			.orElse(GpsPoint.noPoint());
	}

	// 최후 도착 gps point
	private GpsPoint getLastGpsPoint(Long driveId) {
		return gpsHistoryRepository.findLastGpsByDriveId(driveId)
			.map(GpsPoint::from)
			.orElse(GpsPoint.noPoint());
	}

	// toDriveList
	private DriveList toDriveList(DriveEntity drive) {
		GpsPoint on = getFirstGpsPoint(drive.getId());
		GpsPoint off = getLastGpsPoint(drive.getId());

		return new DriveList(
			drive.getId(),
			on.lat(), on.lon(),
			off.lat(), off.lon(),
			drive.getDriveDistance(),
			drive.getDriveOnTime(),
			drive.getDriveOffTime()
		);
	}

	public List<RentDriveHistory> getAllRentHistories(String rentUuid) {
		// rentUuid가 null이거나 빈 문자열이면 전체 조회, 아니면 특정 UUID 검색
		List<RentDriveHistory> rents = rentHistoryRepository
			.findRentHistoriesByRentUuid((rentUuid == null || rentUuid.isEmpty()) ? null : rentUuid)
			.stream()
			.sorted(Comparator.comparing(RentDriveHistory::rentStime).reversed())
			.limit(10)
			.collect(Collectors.toList());

		return rents.stream().map(rentDto -> {
			// 각 렌트에 연결된 drive 리스트 조회
			List<DriveEntity> drives = driveHistoryRepository.findAllByRent_RentUuid(rentDto.rentUuid());

			List<DriveList> driveList = drives.stream()
				.map(this::toDriveList)
				.collect(Collectors.toList());

			// rent + drive 요약 리스트를 포함한 DTO 리턴
			return RentDriveHistory.create(rentDto.rentUuid(), rentDto.renterName(), rentDto.mdn(), rentDto.rentStime(),
				rentDto.rentEtime(), driveList);
		}).collect(Collectors.toList());
	}

	public DriveHistory getDriveHistoriesByDriveId(Long id) {
		DriveEntity drive = driveHistoryRepository.findById(id).orElseThrow(() -> RentException.notFound());

		GpsHistoryEntity onGps = gpsHistoryRepository.findFirstGpsByDriveId(drive.getId()).orElse(null);
		GpsHistoryEntity offGps = gpsHistoryRepository.findLastGpsByDriveId(drive.getId()).orElse(null);

		GpsPoint onPoint = GpsPoint.from(onGps);
		GpsPoint offPoint = GpsPoint.from(offGps);

		RentEntity rent = drive.getRent();
		List<GpsHistoryEntity> gpsList = gpsHistoryRepository.findByDriveId(drive.getId());
		List<GpsData> gpsDataList = GpsData.createList(gpsList);

		return DriveHistory.create(drive.getId(), rent.getRentUuid(), drive.getCar().getMdn(), onPoint.lat(),
			onPoint.lon(), offPoint.lat(), offPoint.lat(), drive.getDriveDistance(), rent.getRenterName(),
			rent.getRenterPhone(), rent.getRentStatus(), rent.getPurpose(), drive.getDriveOnTime(),
			drive.getDriveOffTime(), rent.getRentStime(), rent.getRentEtime(), gpsDataList);
	}

	/**
	 * 차량 mdn 검색으로 차량에 대한 운행정보 가져오기
	 * @param mdn
	 * @return 차량 별 운행기록 list
	 */
	public List<CarDriveHistory> getDriveHistoriesByCar(String mdn) {
		List<DriveEntity> drives = driveHistoryRepository.findAllByCar_Mdn(mdn);

		return drives.stream()
			.map(drive -> {
				GpsPoint on = getFirstGpsPoint(drive.getId());
				GpsPoint off = getLastGpsPoint(drive.getId());
				RentEntity rent = drive.getRent();

				return CarDriveHistory.create(
					drive.getId(),
					rent.getRentUuid(),
					mdn,
					on.lat(), on.lon(),
					off.lat(), off.lon(),
					drive.getDriveDistance(),
					drive.getDriveOnTime(),
					drive.getDriveOffTime()
				);
			})
			.collect(Collectors.toList());
	}

}
