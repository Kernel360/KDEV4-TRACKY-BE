package kernel360.trackycore.core.infrastructure.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel360.trackycore.core.infrastructure.base.DateBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rent")
@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RentEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;		// 대여 ID

	private String mdn;		// 차량식별키

	private String rent_uuid;	// 대여 고유 UUID

	@Column(name = "rent_stime")
	private LocalDateTime rentStime;	// 대여 시작 시간

	@Column(name = "rent_etime")
	private LocalDateTime rentEtime;	// 대여 종료 시간

	@Column(name = "renter_name")
	private String renterName;			// 사용자 이름

	@Column(name = "renter_phone")
	private String renterPhone;		// 사용자 전화번호

	private String purpose;				// 차량 사용 목적

	@Column(name = "rent_status")
	private String rentStatus;			// 대여 상태

	@Column(name = "rent_loc")
	private String rentLoc;			// 대여 위치

	@Column(name = "rent_lat")
	private long rentLat;			// 대여 경도

	@Column(name = "rent_lon")
	private long rentLon;			// 대여 위도

	@Column(name = "return_loc")
	private String returnLoc;			// 반납 위치

	@Column(name = "return_lat")
	private long returnLat;			// 반납 위도

	@Column(name = "return_lon")
	private long returnLon;			// 반납 경도


}