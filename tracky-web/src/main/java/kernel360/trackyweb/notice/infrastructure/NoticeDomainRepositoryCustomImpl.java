package kernel360.trackyweb.notice.infrastructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel360.trackycore.core.domain.entity.NoticeEntity;
import kernel360.trackycore.core.domain.entity.QNoticeEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticeDomainRepositoryCustomImpl implements NoticeDomainRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QNoticeEntity notice = QNoticeEntity.noticeEntity;

	@Override
	public List<NoticeEntity> findAllByIsDeletedFalse() {
		return queryFactory
			.select(notice)
			.from(notice)
			.where(notice.deletedAt.isNull())
			.fetch();
	}
}
