package kernel360.trackycore.core.domain.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kernel360.trackycore.core.domain.base.DateBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "drive")
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DriveEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String mdn;

	@Column(name = "rent_id")
	private long rentId;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private DeviceEntity device;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drive_loc_id")
	private LocationEntity location;

	@Column(name = "drive_distance")
	private String driveDistance;

	@Column(name = "drive_on_time")
	private String driveOnTime;

	@Column(name = "drive_off_time")
	private String driveOffTime;

	public DriveEntity(String mdn, long rentId, DeviceEntity device, LocationEntity location, String driveDistance,
		String driveOnTime, String driveOffTime) {
			
		}
}
