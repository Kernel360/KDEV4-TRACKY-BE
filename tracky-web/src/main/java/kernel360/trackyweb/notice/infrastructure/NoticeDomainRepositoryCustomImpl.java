package kernel360.trackyweb.notice.infrastructure;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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
	public List<NoticeEntity> findByTitleOrContent(String keyword) {
		return queryFactory
			.select(notice)
			.from(notice)
			.where(
				notice.title.containsIgnoreCase(keyword)
					.or(notice.content.containsIgnoreCase(keyword))
					.and(notice.deletedAt.isNull())
			)
			.fetch();
	}

	@Override
	public Page<NoticeEntity> searchNoticeByFilter(String search, Boolean isImportant, Pageable pageable) {
		// 조건 생성
		BooleanBuilder builder = new BooleanBuilder()
			.and(isContainTitleOrContent(search))
			.and(isNotDeleted())
			.and(isImportant(isImportant));

		// 최근 공지사항 순으로 정렬
		JPAQuery<NoticeEntity> query = queryFactory
			.selectFrom(notice)
			.where(builder)
			.orderBy(notice.createdAt.desc());

		List<NoticeEntity> content = fetchPagedContent(query, pageable);

		long total = Optional.ofNullable(
			queryFactory
				.select(notice.count())
				.from(notice)
				.where(builder)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression isContainTitleOrContent(String search) {
		if (StringUtils.isBlank(search)) {
			return null;
		}
		return notice.title.containsIgnoreCase(search)
			.or(notice.content.containsIgnoreCase(search));
	}

	private BooleanExpression isNotDeleted() {
		return notice.deletedAt.isNull();
	}

	private BooleanExpression isImportant(Boolean important) {
		if (important == null)
			return null;

		if (important) {
			return notice.isImportant.isTrue();
		} else {
			return notice.isImportant.isFalse();
		}
	}

	private List<NoticeEntity> fetchPagedContent(JPAQuery<NoticeEntity> query, Pageable pageable) {
		return query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}
}
