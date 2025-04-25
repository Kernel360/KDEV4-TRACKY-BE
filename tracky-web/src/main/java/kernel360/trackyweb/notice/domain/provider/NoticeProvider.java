package kernel360.trackyweb.notice.domain.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.NoticeEntity;
import kernel360.trackyweb.notice.infrastructure.NoticeDomainRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class NoticeProvider {

	private final NoticeDomainRepository noticeDomainRepository;

	public NoticeEntity save(NoticeEntity notice) {
		return noticeDomainRepository.save(notice);
	}

	public List<NoticeEntity> getAll() {
		return noticeDomainRepository.findAllByIsDeletedFalse();
	}

	public NoticeEntity get(Long id) {
		return noticeDomainRepository.findById(id)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.NOTICE_NOT_FOUND));
	}
}
