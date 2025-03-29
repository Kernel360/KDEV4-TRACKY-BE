package kernel360.trackycore.core.domain.entity;

import java.time.LocalDateTime;

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
@Table(name = "biz")
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BizEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "biz_name")
	private String bizName;

	@Column(name = "biz_reg_num")
	private String bizRegNum;

	@Column(name = "biz_admin")
	private String bizAdmin;

	@Column(name = "biz_phone_num")
	private String bizPhoneNum;

	@Column(name = "deleted_at")
	private LocalDateTime deleteAt;

	// 생성 메서드만 공개
	public static BizEntity create(String bizName, String bizRegNum, String bizAdmin, String bizPhoneNum, LocalDateTime deleteAt) {
		BizEntity biz = new BizEntity();
		biz.bizName = bizName;
		biz.bizRegNum = bizRegNum;
		biz.bizAdmin = bizAdmin;
		biz.bizPhoneNum = bizPhoneNum;
		biz.deleteAt = deleteAt;
		return biz;
	}
}