package kernel360trackybe.trackyhub.application.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ApiResponse<T>(String rstCd, String rstMsg, @JsonUnwrapped T data) {

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<T>("000", "Success", data);
	}
}
