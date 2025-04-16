package kernel360.trackycore.core.common.provider;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.entity.LocationEntity;
import kernel360.trackycore.core.infrastructure.repository.LocationRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocationProvider {

	private final LocationRepository locationRepository;

	public LocationEntity save(LocationEntity location) {
		return locationRepository.save(location);
	}
}
