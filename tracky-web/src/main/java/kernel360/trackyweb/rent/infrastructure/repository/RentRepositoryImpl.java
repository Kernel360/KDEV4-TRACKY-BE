package kernel360.trackyweb.rent.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kernel360.trackycore.core.common.entity.RentEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RentRepositoryImpl implements RentRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<RentEntity> searchByFilters(String rentUuid, String rentStatus, LocalDateTime rentDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RentEntity> query = cb.createQuery(RentEntity.class);
		Root<RentEntity> rent = query.from(RentEntity.class);

		List<Predicate> predicates = new ArrayList<>();

		log.info(" searchByFilters : {} {} ",rentStatus, rentDate );

		if (rentUuid != null && !rentUuid.isBlank()) {
			predicates.add(cb.like(rent.get("rentUuid"), "%" + rentUuid + "%"));
		}

		if (rentStatus != null && !rentStatus.isBlank()) {
			predicates.add(cb.equal(rent.get("rentStatus"), rentStatus));
		}

		if (rentDate != null) {
			LocalDateTime startOfDay = rentDate.toLocalDate().atStartOfDay();
			LocalDateTime endOfDay = rentDate.toLocalDate().atTime(23, 59, 59, 999_999_999);

			// rentStime ~ rentEtime 범위에 검색 날짜가 포함되는지 확인
			Predicate startBeforeOrSame = cb.lessThanOrEqualTo(rent.get("rentStime"), endOfDay);
			Predicate endAfterOrSame = cb.greaterThanOrEqualTo(rent.get("rentEtime"), startOfDay);
			predicates.add(cb.and(startBeforeOrSame, endAfterOrSame));
		}

		query.where(cb.and(predicates.toArray(new Predicate[0])));
		return em.createQuery(query).getResultList();
	}
}

