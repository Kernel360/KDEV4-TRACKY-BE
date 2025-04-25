package kernel360.trackycore.core.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel360.trackycore.core.domain.entity.base.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeEntity extends DateBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	private String title;

	private String content;

	private boolean isImportant;

	private LocalDateTime deletedAt;

	private NoticeEntity(MemberEntity member, String title, String content, boolean isImportant) {
		this.member = member;
		this.title = title;
		this.content = content;
		this.isImportant = isImportant;
	}

	public static NoticeEntity create(MemberEntity member, String title, String content, boolean isImportant) {
		NoticeEntity notice = new NoticeEntity(member, title, content, isImportant);
		return notice;
	}

	public void update(String title, String content, boolean isImportant) {
		this.title = title;
		this.content = content;
		this.isImportant = isImportant;
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}
}
