package kernel360.trackycore.core.infrastructure.exception;

/**
 * 공통 모듈 Exception
 */
public class GlobalException extends RuntimeException {

	private final ErrorCode errorCode;

	protected GlobalException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public static GlobalException sendError(ErrorCode errorCode) {
		return new GlobalException(errorCode);
	}

	public static GlobalException throwError(ErrorCode errorCode) {
		throw new GlobalException(errorCode);
	}
}
