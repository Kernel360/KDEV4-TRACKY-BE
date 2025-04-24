package kernel360.trackyweb.notice.presentation;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.notice.application.NoticeService;
import kernel360.trackyweb.notice.application.dto.request.NoticeCreateUpdateRequest;
import kernel360.trackyweb.notice.application.dto.request.NoticeSearchByFilterRequest;
import kernel360.trackyweb.notice.application.dto.response.NoticeDetailResponse;
import kernel360.trackyweb.notice.application.dto.response.NoticeResponse;
import kernel360.trackyweb.sign.infrastructure.security.principal.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

	private final NoticeService noticeService;

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ApiResponse<NoticeDetailResponse> register(@RequestBody NoticeCreateUpdateRequest noticeCreateUpdateRequest,
		@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
		return noticeService.register(memberPrincipal.memberId(), noticeCreateUpdateRequest);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ApiResponse<NoticeDetailResponse> update(@PathVariable Long id,
		@RequestBody NoticeCreateUpdateRequest noticeCreateUpdateRequest) {
		return noticeService.update(id, noticeCreateUpdateRequest);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ApiResponse<NoticeDetailResponse> delete(@PathVariable Long id) {
		return noticeService.delete(id);
	}

	@GetMapping("/search")
	public ApiResponse<List<NoticeDetailResponse>> search(@RequestParam String keyword) {
		return noticeService.search(keyword);
	}

	@GetMapping
	public ApiResponse<List<NoticeResponse>> getAllBySearchFilter(
		@ModelAttribute NoticeSearchByFilterRequest noticeSearchByFilterRequest
	) {
		log.info("입력값:{}", noticeSearchByFilterRequest);
		return noticeService.getAllBySearchFilter(noticeSearchByFilterRequest);
	}
}
