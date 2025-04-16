package kernel360.trackyweb.member.application;

import org.springframework.stereotype.Service;

import kernel360.trackyweb.member.application.validation.MemberValidator;
import kernel360.trackyweb.member.domain.entity.MemberEntity;
import kernel360.trackyweb.member.domain.provider.MemberProvider;
import kernel360.trackyweb.member.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberProvider memberProvider;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberValidator memberValidator;

	/**
	 * 주어진 memberId와 평문 비밀번호를 통해 회원 인증 처리.
	 */
	public MemberEntity authenticate(String memberId, String pwd) {

		log.info("Login attempt for memberId: {}", memberId);

		MemberEntity member = memberProvider.getMember(memberId);

		memberValidator.validatePassword(pwd, member);

		return member;
	}

	public String generateJwtToken(MemberEntity member) {
		String bizName = member.getBizId().getBizName();

		return jwtTokenProvider.generateToken(member.getMemberId(), member.getRole(), bizName);
	}
}
