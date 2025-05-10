package kernel360.trackycore.core.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import kernel360.trackycore.core.domain.entity.base.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "time_distance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeDistanceEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mdn", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private CarEntity car;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "biz_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private BizEntity biz;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "hour", nullable = false)
	private int hour;

	@Column(name = "distance", nullable = false)
	private double distance;

	@Column(name = "seconds", nullable = false)
	private int seconds;

	@Version
	private Long version;

	private TimeDistanceEntity(
		CarEntity car,
		BizEntity biz,
		LocalDate date,
		int hour,
		double distance,
		int seconds
	) {

		this.car = car;
		this.biz = biz;
		this.date = date;
		this.hour = hour;
		this.distance = distance;
		this.seconds = seconds;
	}

	public static TimeDistanceEntity create(CarEntity car, BizEntity biz, LocalDate date, int hour, double distance,
		int seconds) {
		return new TimeDistanceEntity(car, biz, date, hour, distance, seconds);
	}

	public void updateDistance(double distance, int seconds) {
		this.distance += distance;
		this.seconds += seconds;
	}
}
