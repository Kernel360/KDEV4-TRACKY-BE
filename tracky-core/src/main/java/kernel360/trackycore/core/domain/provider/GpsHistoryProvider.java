package kernel360.trackycore.core.domain.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.domain.entity.GpsHistoryEntity;
import kernel360.trackycore.core.infrastructure.repository.GpsHistoryRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GpsHistoryProvider {

	private final GpsHistoryRepository gpsHistoryRepository;

	public GpsHistoryEntity save(GpsHistoryEntity gpsHistory) {
		return gpsHistoryRepository.save(gpsHistory);
	}

	public void saveAll(List<GpsHistoryEntity> gpsHistoryList) {
		gpsHistoryRepository.saveAll(gpsHistoryList);
	}
}
