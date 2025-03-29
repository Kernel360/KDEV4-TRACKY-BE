package kernel360.trackycore.core.infrastructure.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kernel360.trackycore.core.infrastructure.base.DateBaseEntity;
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
	private long driveStartLon;

	@Column(name = "drive_start_lat")
	private long driveStartLat;

	@Column(name = "drive_end_loc")
	private String driveEndLoc;

	@Column(name = "drive_end_lon")
	private long driveEndLon;

	@Column(name = "drive_end_lat")
	private long driveEndLat;

	public static LocationEntity create(
            String driveStartLoc,
			long driveStartLon,
			long driveStartLat) {

        LocationEntity location = new LocationEntity();
        location.driveStartLoc = driveStartLoc;
        location.driveStartLon = driveStartLon;
        location.driveStartLat = driveStartLat;

        return location;
    }
}
