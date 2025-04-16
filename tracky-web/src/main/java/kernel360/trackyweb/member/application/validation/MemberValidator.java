package kernel360.trackyweb.member.application.validation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kernel360.trackycore.core.infrastructure.exception.ErrorCode;
import kernel360.trackyweb.member.domain.entity.MemberEntity;
import kernel360.trackyweb.member.infrastructure.exception.MemberException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator {

	private final PasswordEncoder passwordEncoder;

	public void validatePassword(String pwd, MemberEntity member) {
		if (!passwordEncoder.matches(pwd, member.getPwd())) {
			throw MemberException.sendError(ErrorCode.MEMBER_WRONG_PWD);
		}
	}

}
