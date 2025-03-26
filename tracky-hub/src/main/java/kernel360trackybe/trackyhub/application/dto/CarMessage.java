package kernel360trackybe.trackyhub.application.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarMessage {
	private String messageType; // "GPS" 또는 "DISTANCE"
	private GPSDto gpsData;
	private DistanceDto distanceData;
}