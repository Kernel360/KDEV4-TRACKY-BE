package kernel360trackybe.trackyhub.domain;

import jakarta.persistence.*;
import kernel360trackybe.trackyhub.application.dto.GPSDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@Table(name = "History")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;              // 주행 ID

	private String mdn;           // 차량 번호
	private String tid;           // 터미널 아이디
	private String mid;           // 제조사 아이디
	private String did;           // 디바이스 아이디

	@Column(name = "o_time")
	private LocalDateTime oTime;         // 발생 시간
	private String pv;            // 패킷 버전
	private String gcd;           // GPS 상태
	private long lat;           // GPS 위도
	private long lon;           // GPS 경도
	private String ang;           // 방향
	private String spd;           // 속도

	@Column(name = "on_time")
	private LocalDateTime onTime;  // 시동 건 시각

	@Column(name = "off_time")
	private LocalDateTime offTime; // 시동 끈 시각

	public static History from(GPSDto carInfo) {
		return History.builder()
			.mdn(carInfo.getMdn())
			.tid(carInfo.getTid())
			.mid(carInfo.getMid())
			.did(carInfo.getDid())
			.oTime(carInfo.getOTime())
			.pv(carInfo.getPv())
			.gcd(carInfo.getGcd())
			.lat(carInfo.getLat())
			.lon(carInfo.getLon())
			.ang(carInfo.getAng())
			.spd(carInfo.getSpd())
			.build();
	}
}
