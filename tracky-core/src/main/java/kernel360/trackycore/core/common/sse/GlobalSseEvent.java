package kernel360.trackycore.core.common.sse;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class GlobalSseEvent {
	private String event;
	private String method;
	private String message;
	private LocalDateTime createdAt;

	private GlobalSseEvent(String event, String method, String message) {
		this.event = event;
		this.method = method;
		this.message = message;
		this.createdAt = LocalDateTime.now();
	}

	public static GlobalSseEvent create(String event, String method, String message) {
		return new GlobalSseEvent(event, method, message);
	}

	public GlobalSseEvent sendEvent(SseEvent sseEvent) {
		return new GlobalSseEvent(sseEvent.getEvent(), sseEvent.getMethod(), sseEvent.getMessage());
	}
}
