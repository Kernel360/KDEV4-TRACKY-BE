package kernel360.trackycore.core.domain.entity;

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
@Table(name = "device")
@NoArgsConstructor
@ToString
public class DeviceEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tid;

	private String mid;

	private String did;

	private String pv;

	public static DeviceEntity create(Long id, String tid, String mid, String did, String pv) {
		DeviceEntity device = new DeviceEntity();
		device.id = id;
		device.tid = tid;
		device.mid = mid;
		device.did = did;
		device.pv = pv;
		return device;
	}
}
