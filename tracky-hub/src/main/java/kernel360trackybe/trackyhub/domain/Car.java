package kernel360trackybe.trackyhub.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "car")
@Getter
@NoArgsConstructor
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;              // 차량 ID
	private String mdn;           // 차량식별기

	// 외래키
	@Column(name = "biz_id")
	private String bizId;         // 업체 ID
	
	@Column(name = "car_type")
	private String carType;       // 차종
	
	@Column(name = "car_plate")
	private String carPlate;      // 번호판
	
	@Column(name = "car_year")
	private String carYear;       // 연식
	
	private String purpose;       // 차량용도
	private String status;        // 차량상태
	private int sum;           // 누적 주행 거리

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;   // 생성 시간

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;   // 수정 시간

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;   // 삭제 시간

	public void updateSum(int sum) {
		this.sum += sum;
	}
}
