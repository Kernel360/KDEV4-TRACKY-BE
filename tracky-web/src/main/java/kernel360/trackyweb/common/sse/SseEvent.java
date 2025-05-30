package kernel360.trackyweb.common.sse;

import lombok.Getter;

@Getter
public enum SseEvent {
	CAR_CREATED("car_event", "create", "차량을 등록 하였습니다."),
	CAR_UPDATED("car_event", "update", "차량을 수정 하였습니다."),
	CAR_DELETED("car_event", "delete", "차량을 삭제 하였습니다."),

	RENT_CREATED("rent_event", "create", "렌트를 등록 하였습니다."),
	RENT_UPDATED("rent_event", "update", "렌트를 수정 하였습니다."),
	RENT_DELETED("rent_event", "delete", "렌트를 삭제 하였습니다.");

	private final String event;
	private final String method;
	private final String message;

	SseEvent(String event, String method, String message) {
		this.event = event;
		this.method = method;
		this.message = message;
	}

}
