package kernel360.trackyconsumer.timedistance.infrastructure.repository;

import kernel360.trackycore.core.domain.entity.TimeDistanceEntity;
import kernel360.trackycore.core.infrastructure.repository.TimeDistanceRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeDistanceDomainRepository extends TimeDistanceRepository, TimeDistanceRepositoryCustom {

    @Modifying
    @Query(
            value = "INSERT INTO time_distance (mdn, biz_id, date, hour, distance, seconds, created_at, updated_at, version) " +
                    "VALUES (:#{#entity.car.mdn}, :#{#entity.biz.id}, :#{#entity.date}, :#{#entity.hour}, :#{#entity.distance}, :#{#entity.seconds}, NOW(), NOW(), 0) " +
                    "ON DUPLICATE KEY UPDATE " + // 4. 중복 키 발생 시 업데이트 로직
                    "biz_id = VALUES(biz_id), " + // biz_id는 충돌 시 새로 삽입되는 값으로 업데이트
                    "distance = time_distance.distance + VALUES(distance), " +
                    "seconds = time_distance.seconds + VALUES(seconds), " +
                    "updated_at = NOW(), " +
                    "version = time_distance.version + 1",
            nativeQuery = true
    )
    void upsert(@Param("entity") TimeDistanceEntity timeDistance);
}