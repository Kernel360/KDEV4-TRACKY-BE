package kernel360.trackyweb.notice.application.dto.request;

public record NoticeCreateRequest(
	String title,
	String content,
	boolean isImportant
) {
	public NoticeCreateRequest(
		String title, String content, boolean isImportant
	) {
		this.title = title;
		this.content = content;
		this.isImportant = isImportant;
	}
}
