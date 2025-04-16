package kernel360.trackyweb.member.domain.provider;

import org.springframework.stereotype.Component;

import kernel360.trackycore.core.infrastructure.exception.ErrorCode;
import kernel360.trackyweb.member.domain.entity.MemberEntity;
import kernel360.trackyweb.member.infrastructure.exception.MemberException;
import kernel360.trackyweb.member.infrastructure.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberProvider {

	private final MemberRepository memberRepository;

	public MemberEntity getMember(String memberId) {
		return memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> MemberException.sendError(ErrorCode.MEMBER_NOT_FOUND));
	}
}
