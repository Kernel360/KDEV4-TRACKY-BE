package kernel360.trackyweb.biz.domain.provider;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.BizEntity;
import kernel360.trackyweb.biz.infrastructure.repository.BizDomainRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BizDomainProvider {

	private final BizDomainRepository bizDomainRepository;

	public void save(BizEntity biz) {
		bizDomainRepository.save(biz);
	}

	public boolean existsByBizRegNum(String bizRegNum) {
		return bizDomainRepository.existsByBizRegNum(bizRegNum);
	}

	public BizEntity getBizByBizName(String bizName) {
		
		return bizDomainRepository.findByBizName(bizName)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.BIZ_NOT_FOUND));
	}
}
