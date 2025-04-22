package kernel360.trackycore.core.domain.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel360.trackycore.core.domain.entity.base.DateBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "biz_id")
	private BizEntity bizId;

	@Column(name = "member_id")
	private String memberId;

	private String pwd;

	private String email;

	private String role;

	private String status;

	@Column(name = "lastlogin_at")
	private LocalDateTime lastLoginAt;

	@Column(name = "deleted_at")
	private LocalDateTime deleteAt;

	public static MemberEntity create(BizEntity biz, String memberId, String pwd, String email, String role,
		String status) {
		MemberEntity member = new MemberEntity();
		member.bizId = biz;
		member.memberId = memberId;
		member.pwd = pwd;
		member.email = email;
		member.role = role;
		member.status = status;
		return member;
	}

}
