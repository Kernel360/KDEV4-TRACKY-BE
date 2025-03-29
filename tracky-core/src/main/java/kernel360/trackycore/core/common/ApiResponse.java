package kernel360.trackycore.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ApiResponse<T> {
	private int status;
	private String message;
	private T data;

	public static <T> ApiResponse<T> success(T data) {
		return ApiResponse.of(200, "success", data);
	}

	public static <T> ApiResponse<T> fail(int status, String message) {
		return ApiResponse.of(status, message, null);
	}
}
