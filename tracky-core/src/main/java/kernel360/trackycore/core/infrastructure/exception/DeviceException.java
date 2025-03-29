package kernel360.trackycore.core.infrastructure.exception;

/**
 * 디바이스 관련 공통 Exception
 */
public class DeviceException extends GlobalException {

	private DeviceException(ErrorCode errorCode) {
		super(errorCode);
	}

	public static DeviceException notFound() {
		return new DeviceException(ErrorCode.DEVICE_NOT_FOUND);
	}
}
