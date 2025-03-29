package kernel360.trackyweb.member.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	// 생성자 주입
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * 요청마다 실행되며, Authorization 헤더에서 JWT 토큰을 추출해 유효성을 검사.
	 * 유효한 토큰이면 SecurityContext에 인증 정보를 설정함.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
		throws ServletException, IOException {

		String token = resolveToken(request);

		if (token != null && jwtTokenProvider.validateToken(token)) {

			String memberId = jwtTokenProvider.getMemberId(token);
			String admin = jwtTokenProvider.getAdmin(token);

			// 추출한 값을 GrantedAuthority로 변환
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(
					memberId,
					null,
					Collections.singletonList(new SimpleGrantedAuthority(admin))
				);

			// SecurityContext에 Authentication 객체를 설정합니다.
			SecurityContextHolder.getContext().setAuthentication(authentication);

			logger.info("Authentication set for memberId: {}", memberId);
		} else {
			logger.warn("JWT token is missing or invalid");
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * HTTP Authorization 헤더에서 "Bearer " 접두어를 제거하고 토큰만 반환.
	 *
	 * @param request HTTP 요청 객체
	 * @return JWT 토큰 문자열 또는 null
	 */
	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}
}
