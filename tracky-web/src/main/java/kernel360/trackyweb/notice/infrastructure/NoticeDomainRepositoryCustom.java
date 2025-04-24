package kernel360.trackyweb.notice.infrastructure;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kernel360.trackycore.core.domain.entity.NoticeEntity;

public interface NoticeDomainRepositoryCustom {

	List<NoticeEntity> findByTitleOrContent(String keyword);

	Page<NoticeEntity> searchNoticeByFilter(String search, Boolean isImportant, Pageable pageable);
}
