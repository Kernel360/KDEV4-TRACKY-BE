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
import jakarta.persistence.JoinColumn;
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

	@JoinColumn(name = "drive_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private DriveEntity drive;

	@Column(name = "o_time")
	private LocalDateTime oTime;

	private String gcd;

	private long lat;

	private long lon;

	private int ang;

	private int spd;

	private int sum;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	public GpsHistoryEntity(DriveEntity drive, LocalDateTime oTime, String gcd, long lat, long lon, int ang, int spd, int sum) {
	    this.drive = drive;
	    this.oTime = oTime;
	    this.gcd = gcd;
	    this.lat = lat;
	    this.lon = lon;
	    this.ang = ang;
	    this.spd = spd;
	    this.sum = sum;
	}
}
