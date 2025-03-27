package kernel360.trackycore.core.infrastructure.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "gpshistory")
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GpsHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "drive_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private DriveEntity drive;

	@Column(name = "o_time")
	private String oTime;

	private String gcd;

	private String lat;

	private String lon;

	private String ang;

	private String spd;

	private String sum;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	public GpsHistoryEntity(DriveEntity drive, LocalDateTime oTime, String gcd, long lat, long lon, String ang, String spd, int sum) {
	}
}
