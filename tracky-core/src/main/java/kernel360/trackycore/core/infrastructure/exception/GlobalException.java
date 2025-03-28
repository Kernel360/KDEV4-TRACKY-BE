package kernel360.trackycore.core.infrastructure.exception;


public class GlobalException extends RuntimeException {

	private final ErrorCode errorCode;

	protected GlobalException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
