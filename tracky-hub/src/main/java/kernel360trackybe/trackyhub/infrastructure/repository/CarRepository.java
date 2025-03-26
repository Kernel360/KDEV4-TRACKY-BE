package kernel360trackybe.trackyhub.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kernel360trackybe.trackyhub.domain.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	Optional<Car> findByMdn(String mdn);
}
