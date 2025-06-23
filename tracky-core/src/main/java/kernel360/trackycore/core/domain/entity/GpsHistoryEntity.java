package kernel360.trackycore.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.uuid.Generators;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "gpshistory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GpsHistoryEntity implements Persistable<UUID> {

	@Id
	@Column(name = "drive_seq", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
	private UUID driveSeq;    //주행기록 시퀀스

	// 2. 새로운 객체인지 판단하기 위한 상태 필드를 추가합니다.
	// @Transient 어노테이션으로 이 필드가 DB 컬럼에 매핑되지 않도록 합니다.
	@Transient
	private boolean isNew = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drive_id", nullable = false)
	private DriveEntity drive;    //주행ID 외래키

	@Column(name = "o_time", nullable = false)
	private LocalDateTime oTime;    //발생시간

	@Column(name = "gcd", length = 10, nullable = false)
	private String gcd;        //GPS상태

	@Column(name = "lat", nullable = false)
	private int lat;    //GPS위도

	@Column(name = "lon", nullable = false)
	private int lon;    //GPS경도

	@Column(name = "ang", nullable = false)
	private int ang;    //방향

	@Column(name = "spd", nullable = false)
	private int spd;    //속도

	@Column(name = "sum", nullable = false)
	private double sum;    //단건 주행거리

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;    //생성시간

	public GpsHistoryEntity(DriveEntity drive, LocalDateTime oTime, String gcd, int lat, int lon, int ang,
		int spd,
		double sum) {
		this.driveSeq = Generators.timeBasedEpochGenerator().generate();
		this.drive = drive;
		this.oTime = oTime;
		this.gcd = gcd;
		this.lat = lat;
		this.lon = lon;
		this.ang = ang;
		this.spd = spd;
		this.sum = sum;
	}

	// --- Persistable 인터페이스 구현을 위한 메서드들 ---

	@Override
	public UUID getId() {
		return this.driveSeq;
	}

	@Override
	public boolean isNew() {
		// 3. 'isNew' 필드 값을 반환하여 JPA가 새로운 객체인지 판단하도록 합니다.
		return this.isNew;
	}

	// 4. @PrePersist 어노테이션: 해당 엔티티가 저장(persist)되기 직전에 호출됩니다.
	// save()가 처음 호출될 때만 동작합니다.
	@PrePersist
	void prePersist() {
		this.isNew = false;
	}

	// 5. @PostLoad 어노테이션: DB에서 데이터를 조회한 후 엔티티가 로드될 때 호출됩니다.
	// find() 등으로 조회된 객체는 새로운 객체가 아니므로 isNew를 false로 설정해줍니다.
	@PostLoad
	void postLoad() {
		this.isNew = false;
	}
}
