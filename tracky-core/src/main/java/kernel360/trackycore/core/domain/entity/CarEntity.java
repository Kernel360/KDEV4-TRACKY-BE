package kernel360.trackycore.core.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel360.trackycore.core.domain.base.DateBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car")
@Getter
@NoArgsConstructor
public class CarEntity extends DateBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;              // 차량 ID
	private String mdn;           // 차량식별기

	// 외래키
	// @ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "biz_id")
	private String bizId;         // 업체 ID

	// 외래키
	@JoinColumn(name = "device_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private DeviceEntity device;

	@Column(name = "car_type")
	private String carType;       // 차종

	@Column(name = "car_plate")
	private String carPlate;      // 번호판

	@Column(name = "car_year")
	private String carYear;       // 연식

	private String purpose;       // 차량용도
	private String status;        // 차량상태
	private	String sum;           // 누적 주행 거리

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;   // 삭제 시간

	public void updateSum(int sum) {
		this.sum += sum;
	}

	public static CarEntity create(
		Long id,
		String mdn,
		String bizId,
		DeviceEntity device,
		String carType,
		String carPlate,
		String carYear,
		String purpose,
		String status,
		String sum
	) {
		CarEntity car = new CarEntity();
		car.id = id;
		car.mdn = mdn;
		car.bizId = bizId;
		car.device = device;
		car.carType = carType;
		car.carPlate = carPlate;
		car.carYear = carYear;
		car.purpose = purpose;
		car.status = status;
		car.sum = sum;
		car.createdAt = LocalDateTime.now();
		return car;
	}
}
