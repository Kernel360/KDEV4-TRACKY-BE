package kernel360.trackycore.core.domain.provider;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.common.exception.ErrorCode;
import kernel360.trackycore.core.common.exception.GlobalException;
import kernel360.trackycore.core.domain.entity.DeviceEntity;
import kernel360.trackycore.core.infrastructure.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceProvider {

	private final DeviceRepository deviceRepository;

	public DeviceEntity getDevice(Long id) {
		return deviceRepository.findById(id)
			.orElseThrow(() -> GlobalException.throwError(ErrorCode.DEVICE_NOT_FOUND));
	}
}
