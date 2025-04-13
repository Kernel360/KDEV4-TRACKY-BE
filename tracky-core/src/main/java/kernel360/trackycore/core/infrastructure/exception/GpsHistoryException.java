package kernel360.trackycore.core.infrastructure.exception;

public class GpsHistoryException extends GlobalException {
	public GpsHistoryException(ErrorCode errorCode) {
		super(errorCode);
	}

	public static GpsHistoryException notFound() {
		return new GpsHistoryException(ErrorCode.GPSHISTORY_NOT_FOUND);
	}
}
