package kernel360.trackyweb.notice.infrastructure;

import java.util.List;

import kernel360.trackycore.core.domain.entity.NoticeEntity;

public interface NoticeDomainRepositoryCustom {

	List<NoticeEntity> findAllByIsDeletedFalse();

	List<NoticeEntity> findByTitleOrContent(String keyword);
}
