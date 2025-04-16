package kernel360trackybe.trackyhub.application.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
	private String rstCd;
	private String rstMsg;
	@JsonUnwrapped
	private T data;

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<T>("000", "Success", data);
	}
}
