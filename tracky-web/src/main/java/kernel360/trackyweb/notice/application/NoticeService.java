package kernel360.trackyweb.notice.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackycore.core.common.api.PageResponse;
import kernel360.trackycore.core.domain.entity.MemberEntity;
import kernel360.trackycore.core.domain.entity.NoticeEntity;
import kernel360.trackyweb.notice.application.dto.request.NoticeCreateUpdateRequest;
import kernel360.trackyweb.notice.application.dto.request.NoticeSearchByFilterRequest;
import kernel360.trackyweb.notice.application.dto.response.NoticeDetailResponse;
import kernel360.trackyweb.notice.application.dto.response.NoticeResponse;
import kernel360.trackyweb.notice.domain.provider.NoticeProvider;
import kernel360.trackyweb.sign.domain.provider.MemberProvider;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoticeService {
	private final NoticeProvider noticeProvider;
	private final MemberProvider memberProvider;

	@Transactional
	public ApiResponse<NoticeDetailResponse> register(String memberId,
		NoticeCreateUpdateRequest noticeCreateUpdateRequest) {

		MemberEntity member = memberProvider.getMember(memberId);

		NoticeEntity notice = NoticeEntity.create(
			member,
			noticeCreateUpdateRequest.title(),
			noticeCreateUpdateRequest.content(),
			noticeCreateUpdateRequest.isImportant()
		);

		NoticeEntity savedNotice = noticeProvider.save(notice);

		NoticeDetailResponse response = NoticeDetailResponse.from(savedNotice);
		return ApiResponse.success(response);
	}

	@Transactional
	public ApiResponse<NoticeDetailResponse> update(Long id, NoticeCreateUpdateRequest noticeCreateUpdateRequest) {

		NoticeEntity notice = noticeProvider.getById(id);
		notice.update(noticeCreateUpdateRequest.title(), noticeCreateUpdateRequest.content(),
			noticeCreateUpdateRequest.isImportant());

		NoticeDetailResponse response = NoticeDetailResponse.from(notice);

		return ApiResponse.success(response);
	}

	@Transactional
	public ApiResponse<NoticeDetailResponse> delete(Long id) {

		NoticeEntity notice = noticeProvider.getById(id);

		notice.softDelete();

		NoticeEntity updatedNotice = noticeProvider.save(notice);

		NoticeDetailResponse response = NoticeDetailResponse.from(updatedNotice);
		return ApiResponse.success(response);
	}

	@Transactional
	public ApiResponse<List<NoticeDetailResponse>> search(String keyword) {
		List<NoticeEntity> notices = noticeProvider.search(keyword);

		List<NoticeDetailResponse> responses = notices.stream()
			.map(NoticeDetailResponse::from)
			.toList();

		return ApiResponse.success(responses);
	}

	public ApiResponse<List<NoticeResponse>> getAllBySearchFilter(
		NoticeSearchByFilterRequest noticeSearchByFilterRequest) {

		Page<NoticeEntity> notices = noticeProvider.searchNoticeByFilter(
			noticeSearchByFilterRequest.search(),
			noticeSearchByFilterRequest.isImportant(),
			noticeSearchByFilterRequest.toPageable());

		Page<NoticeResponse> noticeResponses = notices.map(NoticeResponse::from);
		PageResponse pageResponse = PageResponse.from(noticeResponses);

		return ApiResponse.success(noticeResponses.getContent(), pageResponse);
	}
}
