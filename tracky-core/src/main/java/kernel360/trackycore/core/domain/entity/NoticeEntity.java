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
	@Column(name = "id", updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity member;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "is_important", nullable = false)
	private boolean isImportant;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	private NoticeEntity(MemberEntity member, String title, String content, boolean isImportant) {
		this.member = member;
		this.title = title;
		this.content = content;
		this.isImportant = isImportant;
	}

	public static NoticeEntity create(MemberEntity member, String title, String content, boolean isImportant) {
		NoticeEntity notice = new NoticeEntity(member, title, content, isImportant);
		notice.onCreate();
		return notice;
	}

	public void update(String title, String content, boolean isImportant) {
		this.title = title;
		this.content = content;
		this.isImportant = isImportant;
		this.onUpdate();
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}
}
