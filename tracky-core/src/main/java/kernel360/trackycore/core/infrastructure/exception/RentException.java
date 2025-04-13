package kernel360.trackycore.core.infrastructure.exception;

public class RentException extends GlobalException {
	public RentException(ErrorCode errorCode) {
		super(errorCode);
	}

	public static RentException notFound() {
		return new RentException(ErrorCode.RENT_NOT_FOUND);
	}
}
