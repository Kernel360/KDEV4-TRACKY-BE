package kernel360trackybe.trackyhub.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistanceDto {
    private String mdn;  // 차량 번호
    private int distance; // 누적 주행 거리
}