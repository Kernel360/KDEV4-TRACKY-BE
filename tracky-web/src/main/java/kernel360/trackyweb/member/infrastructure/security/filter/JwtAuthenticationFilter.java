package kernel360.trackyweb.member.infrastructure.security.filter;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel360.trackyweb.member.infrastructure.security.jwt.JwtTokenProvider;
import kernel360.trackyweb.member.infrastructure.security.validation.JwtValidation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtValidation jwtValidation;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	/**
	 * 요청마다 실행되며, Authorization 헤더에서 JWT 토큰을 추출해 유효성을 검사.
	 * 유효한 토큰이면 SecurityContext에 인증 정보를 설정함.
	 */
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		if (checkSseEvent(request, response, filterChain)) {
			return;
		}

		if (checkAlbHealthCheck(request, response, filterChain)) {
			return;
		}

		String token = resolveToken(request);

		if (jwtValidation.isValidToken(token)) {
			String memberId = jwtTokenProvider.getMemberId(token);
			String role = jwtTokenProvider.getRole(token).toUpperCase();
			String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(
					memberId,
					null,
					Collections.singletonList(new SimpleGrantedAuthority(authority))
				);

			// SecurityContext에 Authentication 객체를 설정
			SecurityContextHolder.getContext().setAuthentication(authentication);
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
		} else {
			return null;
		}
	}

	/**
	 * SSE 요청은 토큰 없이 통과시킴
	 * @param request
	 * @return boolean
	 */
	private boolean checkSseEvent(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		if (request.getRequestURI().startsWith("/events")) {
			filterChain.doFilter(request, response);
			return true;
		}
		return false;
	}

	/**
	 * ALB HealthCheck 허용
	 * @param request
	 * @return boolean
	 */
	private boolean checkAlbHealthCheck(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		if (request.getRequestURI().startsWith("/actuator")) {
			filterChain.doFilter(request, response);
			return true;
		}
		return false;
	}

}
