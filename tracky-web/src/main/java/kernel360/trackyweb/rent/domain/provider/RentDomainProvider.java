package kernel360.trackyweb.rent.domain.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import kernel360.trackyweb.common.entity.RentEntity;
import kernel360.trackyweb.common.provider.RentProvider;
import kernel360.trackyweb.rent.infrastructure.repository.CarRentRepository;
import kernel360.trackyweb.rent.infrastructure.repository.RentDomainRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RentDomainProvider {

	private final RentDomainRepository rentDomainRepository;
	private final CarRentRepository carRentRepository;
	private final RentProvider rentProvider;

	public RentEntity getRent(String rentUuid) {
		return rentProvider.findByRentUuid(rentUuid);
	}

	public RentEntity save(RentEntity rent) {
		return rentDomainRepository.save(rent);
	}

	public void delete(String rentUuid) {
		rentDomainRepository.deleteByRentUuid(rentUuid);
	}

	public List<String> findAllMdns() {
		return carRentRepository.findAllMdns();
	}

}
