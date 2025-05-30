package kernel360.trackycore.core.domain.provider;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.BizEntity;
import kernel360.trackycore.core.infrastructure.repository.BizRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BizProvider {

	private final BizRepository bizRepository;

	public BizEntity getBiz(Long id) {
		return bizRepository.findById(id)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.BIZ_NOT_FOUND));
	}
}
