package kernel360.trackyweb.notice.presentation;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel360.trackycore.core.common.api.ApiResponse;
import kernel360.trackyweb.notice.application.NoticeService;
import kernel360.trackyweb.notice.application.dto.request.NoticeRequest;
import kernel360.trackyweb.notice.application.dto.response.NoticeDetailResponse;
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
	public ApiResponse<NoticeDetailResponse> register(@RequestBody NoticeRequest noticeRequest,
		@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
		return noticeService.register(memberPrincipal.memberId(), noticeRequest);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ApiResponse<NoticeDetailResponse> update(@PathVariable Long id, @RequestBody NoticeRequest noticeRequest) {
		return noticeService.update(id, noticeRequest);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ApiResponse<NoticeDetailResponse> delete(@PathVariable Long id) {
		return noticeService.delete(id);
	}

	@GetMapping
	public ApiResponse<List<NoticeDetailResponse>> getAll() {
		return noticeService.getAll();
	}

}
