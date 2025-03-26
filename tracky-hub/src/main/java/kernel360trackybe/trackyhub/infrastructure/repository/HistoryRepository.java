package kernel360trackybe.trackyhub.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel360trackybe.trackyhub.domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    
}
