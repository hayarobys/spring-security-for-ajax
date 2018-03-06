package com.suph.security.web.csrf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

public final class CustomCsrfFilter extends OncePerRequestFilter{
	public static final RequestMatcher DEFAULT_CSRF_MATCHER = new DefaultRequiresCsrfMatcher();
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private final CsrfTokenRepository tokenRepository;
	private RequestMatcher requireCsrfProtectionMatcher = DEFAULT_CSRF_MATCHER;
	private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
	
	public CustomCsrfFilter(CsrfTokenRepository csrfTokenRepository){
		Assert.notNull(csrfTokenRepository, "csrfTokenRepository cannot be null");
		this.tokenRepository = csrfTokenRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException{
		
		// 요청에 대한 응답 객체를 바꿔치기 합니다.
		LOGGER.debug("응답객체를 이 이름으로 요청객체에 저장합니다: {}", HttpServletResponse.class.getName());
		request.setAttribute(HttpServletResponse.class.getName(), response);
		
		// 요청으로부터 CSRF 토큰을 추출합니다. 이 경우 CookieCsrfTokenRepository를 사용하므로 쿠키로부터 구합니다.
		CsrfToken csrfToken = this.tokenRepository.loadToken(request);
		final boolean missingToken = (csrfToken == null);
		
		if(missingToken){
			// 쿠키에 CSRF토큰이 없다면, 토큰을 새로 발급합니다.
			LOGGER.debug("쿠키로부터 CSRF토큰을 발견하지 못했으므로, CSRF토큰을 재발급 합니다.");
			csrfToken = this.tokenRepository.generateToken(request);
			
			// 재생성한 토큰을 응답 쿠키에 저장합니다.
			this.tokenRepository.saveToken(csrfToken, request, response);
		}
		
		// 요청받았거나, 새로생성한 CSRF토큰을 그대로 응답합니다.
		request.setAttribute(CsrfToken.class.getName(), csrfToken);
		request.setAttribute(csrfToken.getParameterName(), csrfToken);
		
		if(!this.requireCsrfProtectionMatcher.matches(request)){
		// CSRF보호가 불필요한 HTTP메서드 요청이라면 다른 필터로 제어 넘김.
			filterChain.doFilter(request, response);
			return;
		}
		
		// 요청 헤더로부터 csrf토큰을 추출합니다.
		String actualToken = request.getHeader(csrfToken.getHeaderName());
		if(actualToken == null){
			// 헤더에 csrf토큰이 없다면, 다른 이름으로 찾아봅니다.
			LOGGER.debug("지정된 헤더명에서 CSRF 토큰을 찾지못했기에 다른 파라미터 명으로 추출을 시도합니다.");
			actualToken = request.getParameter(csrfToken.getParameterName());
		}
		
		if(!csrfToken.getToken().equals(actualToken)){
			// 쿠키속 토큰과 헤더속 토큰이 불일치한다면
			// 에러 메시지를 띄우고
			LOGGER.debug("다음의 요청으로부터 잘못된 CSRF 토큰이 발견되었습니다: "
					+ UrlUtils.buildFullRequestUrl(request)
			);
			
			if(missingToken){
				// 쿠키에 CSRF 토큰이 없는거면 헤더속 토큰이 쿠키에 없다고 예외 발생
				this.accessDeniedHandler.handle(request, response, new MissingCsrfTokenException(actualToken));
			}else{
				// 토큰이 있는데도 쿠키와 헤더의 토큰들이 불일치한거면 불일치했다고 예외 발생
				this.accessDeniedHandler.handle(request, response, new InvalidCsrfTokenException(csrfToken, actualToken));
			}
			
			return;
		}
		
		// 다음 필터로 제어 넘김
		filterChain.doFilter(request, response);
	}
	
	public void setRequireCsrfProtectionMatcher(
		RequestMatcher requireCsrfProtectionMatcher
	){
		Assert.notNull(requireCsrfProtectionMatcher, "requireCsrfProtectionMAtcher cannot be null");
		this.requireCsrfProtectionMatcher = requireCsrfProtectionMatcher;
	};
	
	public void setAccessDeniedHandler(
		AccessDeniedHandler accessDeniedHandler
	){
		Assert.notNull(accessDeniedHandler, "accessDeniedHandler cannot be null");
		this.accessDeniedHandler = accessDeniedHandler;
	}
	
	// CSRF보호가 필요합니까?
	private static final class DefaultRequiresCsrfMatcher implements RequestMatcher{
		private final HashSet<String> allowedMethods = new HashSet<String>(
			Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS")
		);
		
		/**
		 * 이 요청에 대해 csrf보호(검사)가 필요합니까?
		 * 요청의 HTTP Method가 GET, HEAD, TRACE, OPTIONS중 하나와 일치하는지 검사합니다.
		 * 허용 목록과 하나라도 일치한다면 false,
		 * 허용 목록에 없어서 csrf보호가 필요하다고 판단되었다면 true
		 */
		@Override
		public boolean matches(HttpServletRequest request){
			return !this.allowedMethods.contains(request.getMethod());
		}
	}
}












