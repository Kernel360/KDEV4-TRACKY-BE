package kernel360.trackyconsumer.application.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import kernel360.trackycore.core.infrastructure.entity.LocationEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarOnOffRequest {
    private String mdn;           // 차량 식별 key
    private String tid;           // 차량관제 터미널 ID
    private String mid;           // 제조사 ID
    private String pv;            // 패킷 버전
    private String did;           // 디바이스 ID

	@JsonFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime onTime;  // 시동 On 시간

	@JsonFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime offTime; // 시동 Off 시간
    
    private String gcd;           // GPS 상태
    private long lat;             // GPS 위도
    private long lon;             // GPS 경도
    private String ang;           // 방향
    private String spd;           // 속도(km/h)
    private int sum;              // 누적 주행 거리(m)

    public LocationEntity toLocationEntity() {

		return LocationEntity.create(
			"시작 위치", // 이거 구해야하나? 프론트에서 하는거지?
			this.lon,
			this.lat);
    }
}
