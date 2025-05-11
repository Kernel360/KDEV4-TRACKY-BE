package kernel360.trackyweb.timedistance.application.dto.internal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OperationSeconds(
	long bizId,
	int operationSeconds
) {
	public static Map<Long, Long> toMap(List<OperationSeconds> dtoList) {
		return dtoList.stream()
			.collect(Collectors.toMap(
				OperationSeconds::bizId,
				dto -> (long)dto.operationSeconds()
			));
	}

}

