package kernel360.trackyconsumer.timedistance.domain.provider;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kernel360.trackyconsumer.timedistance.infrastructure.repository.TimeDistanceDomainRepository;
import kernel360.trackycore.core.domain.entity.CarEntity;
import kernel360.trackycore.core.domain.entity.TimeDistanceEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimeDistanceDomainProvider {

	private final TimeDistanceDomainRepository timeDistanceDomainRepository;

	public void save(TimeDistanceEntity timeDistance) {
		timeDistanceDomainRepository.save(timeDistance);
	}

	public void upsert(TimeDistanceEntity timeDistance) {
		timeDistanceDomainRepository.upsert(timeDistance);
	}

	public Optional<TimeDistanceEntity> getTimeDistance(LocalDate date, int hour, CarEntity car) {
		return Optional.ofNullable(timeDistanceDomainRepository.findByDateAndHourAndCar(date, hour, car));
	}
}
