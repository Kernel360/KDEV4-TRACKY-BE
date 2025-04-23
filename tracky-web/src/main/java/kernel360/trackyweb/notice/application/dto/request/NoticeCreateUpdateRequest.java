package kernel360.trackyweb.notice.application.dto.request;

public record NoticeCreateUpdateRequest(
	String title,
	String content,
	boolean isImportant
) {
	public NoticeCreateUpdateRequest(
		String title, String content, boolean isImportant
	) {
		this.title = title;
		this.content = content;
		this.isImportant = isImportant;
	}
}
