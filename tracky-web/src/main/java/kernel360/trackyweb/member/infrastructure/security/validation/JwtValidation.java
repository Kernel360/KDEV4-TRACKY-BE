package kernel360.trackyweb.member.infrastructure.security.validation;

import org.springframework.stereotype.Component;

import kernel360.trackyweb.member.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidation {

	private final JwtTokenProvider jwtTokenProvider;

	public boolean isValidToken(String token) {
		return token != null && jwtTokenProvider.validateToken(token);
	}
}
