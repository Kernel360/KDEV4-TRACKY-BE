package kernel360.trackycore.core.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kernel360.trackycore.core.domain.base.DateBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "location")
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "drive_start_loc")
	private String driveStartLoc;

	@Column(name = "drive_start_lon")
	private String driveStartLon;

	@Column(name = "drive_start_lat")
	private String driveStartLat;

	@Column(name = "drive_end_loc")
	private String driveEndLoc;

	@Column(name = "drive_end_lon")
	private String driveEndLon;

	@Column(name = "drive_end_lat")
	private String driveEndLat;
}
