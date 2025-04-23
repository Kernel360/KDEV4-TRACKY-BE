package kernel360.trackyweb.notice.application.dto.response;

import java.time.LocalDateTime;

import kernel360.trackycore.core.domain.entity.NoticeEntity;

public record NoticeDetailResponse(
	long id,
	String title,
	String content,
	LocalDateTime createdAt,
	boolean isImportant
) {
	public NoticeDetailResponse(
		long id, String title, String content, LocalDateTime createdAt, boolean isImportant
	) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
		this.isImportant = isImportant;
	}

	public static NoticeDetailResponse from(NoticeEntity notice) {
		return new NoticeDetailResponse(
			notice.getId(),
			notice.getTitle(),
			notice.getContent(),
			notice.getCreatedAt(),
			notice.isImportant()
		);
	}

}
