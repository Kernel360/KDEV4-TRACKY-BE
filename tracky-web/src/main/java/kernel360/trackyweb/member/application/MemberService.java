package kernel360.trackyweb.member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kernel360.trackycore.core.infrastructure.exception.ErrorCode;
import kernel360.trackyweb.member.domain.entity.MemberEntity;
import kernel360.trackyweb.member.domain.provider.MemberProvider;
import kernel360.trackyweb.member.infrastructure.exception.MemberException;
import kernel360.trackyweb.member.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberProvider memberProvider;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 주어진 memberId와 평문 비밀번호를 통해 회원 인증 처리.
	 */
	public MemberEntity authenticate(String memberId, String pwd) {
		log.info("Login attempt for memberId: {}", memberId);
		MemberEntity member = memberProvider.getLoginTarget(memberId);

		if (!passwordEncoder.matches(pwd, member.getPwd())) {
			throw MemberException.sendError(ErrorCode.MEMBER_WRONG_PWD);
		}

		return member;
	}

	public String generateJwtToken(MemberEntity member) {
		String bizName = member.getBizId().getBizName();

		return jwtTokenProvider.generateToken(member.getMemberId(), member.getRole(), bizName);
	}
}
