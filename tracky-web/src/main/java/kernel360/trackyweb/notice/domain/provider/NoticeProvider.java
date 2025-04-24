package kernel360.trackyweb.notice.domain.provider;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.NoticeEntity;
import kernel360.trackyweb.notice.infrastructure.NoticeDomainRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class NoticeProvider {

	private final NoticeDomainRepository noticeDomainRepository;

	public NoticeEntity save(NoticeEntity notice) {
		return noticeDomainRepository.save(notice);
	}

	public NoticeEntity getById(Long id) {
		return noticeDomainRepository.findById(id)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.NOTICE_NOT_FOUND));
	}

	public List<NoticeEntity> search(String keyword) {
		return noticeDomainRepository.findByTitleOrContent(keyword);
	}

	public Page<NoticeEntity> searchNoticeByFilter(String search, Boolean isImportant, Pageable pageable) {
		return noticeDomainRepository.searchNoticeByFilter(search, isImportant, pageable);
	}
}
