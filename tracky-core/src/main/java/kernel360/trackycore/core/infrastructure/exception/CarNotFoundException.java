package kernel360.trackycore.core.infrastructure.exception;

/**
 * 차량 관련 Exception 공통 Exception 사용 할 수 있음
 */
public class CarNotFoundException extends GlobalException {
	public CarNotFoundException() {
		super(ErrorCode.CAR_NOT_FOUND);
	}
}
