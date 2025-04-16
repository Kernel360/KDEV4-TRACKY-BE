package kernel360.trackyweb.member.infrastructure.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${spring.jwt.secret}")
	private String secretKeyRaw;

	@Value("${spring.jwt.expiration}")
	private long expiration;

	private Key secretKey;

	@PostConstruct
	protected void init() {
		this.secretKey = Keys.hmacShaKeyFor(secretKeyRaw.getBytes());
	}

	public String generateToken(String memberId, String role, String bizName) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expiration);

		return Jwts.builder()
			.setSubject(memberId)
			.claim("role", role)
			.claim("bizName", bizName)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String getMemberId(String token) {
		return parseClaims(token).getBody().getSubject();
	}

	public String getRole(String token) {
		return parseClaims(token).getBody().get("role", String.class);
	}

	/**
	 * 토큰의 유효성을 검사 (예: 만료, 변조 여부 등)
	 */
	public boolean validateToken(String token) {
		if (token == null)
			return false;
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}
}
