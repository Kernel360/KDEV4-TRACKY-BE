package kernel360.trackycore.core.infrastructure.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	CAR_NOT_FOUND("CAR_001", "조회된 차량이 없습니다."),
	CAR_DUPLICATED("CAR_002", "중복된 차량 번호입니다."),
	DEVICE_NOT_FOUND("DEVICE_001", "조회된 장치가 없습니다."),
	BIZ_NOT_FOUND("BIZ_001", "조회된 업체가 없습니다."),
	RENT_NOT_FOUND("RENT_001", "조회된 대여가 없습니다."),
	GPSHISTORY_NOT_FOUND("GPSHISTORY_001", "조회된 gps data가 없습니다.");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
