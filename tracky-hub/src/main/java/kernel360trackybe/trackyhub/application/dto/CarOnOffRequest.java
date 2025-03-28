package kernel360trackybe.trackyhub.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CarOnOffRequest {
    private String mdn;           // 차량 식별 key
    private String tid;           // 차량관제 터미널 ID
    private String mid;           // 제조사 ID
    private String pv;            // 패킷 버전
    private String did;           // 디바이스 ID
    
    private LocalDateTime onTime;  // 시동 On 시간
    private LocalDateTime offTime; // 시동 Off 시간
    
    private String gcd;           // GPS 상태
    private long lat;             // GPS 위도
    private long lon;             // GPS 경도
    private String ang;           // 방향
    private String spd;           // 속도(km/h)
    private int sum;              // 누적 주행 거리(m)
}
