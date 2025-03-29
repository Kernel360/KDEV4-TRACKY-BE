package kernel360.trackyweb.member.jwt;

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

@Component
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

	public String generateToken(String memberId, String admin) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expiration);

		return Jwts.builder()
			.setSubject(memberId)
			.claim("admin", admin)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// 토큰에서 memberId 추출
	public String getMemberId(String token) {
		return parseClaims(token).getBody().getSubject();
	}

	// 토큰에서 DB에 저장된 권한 값(admin)을 추출하는 메서드
	public String getAdmin(String token) {
		return parseClaims(token).getBody().get("admin", String.class);
	}

	// 토큰의 유효성을 검사 (예: 만료, 변조 여부 등)
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			// 예외 상황을 로깅
			//logger.error("Invalid JWT token: {}", e.getMessage());
			return false;
		}
	}

	// 내부적으로 토큰을 파싱하는 메서드
	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}
}
