package com.suph.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;

/***
 * 비로그인 유저가 권한이 필요한 페이지에 접근할 경우 처리동작을 묻기위해 호출되는 클래스.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
	/** Ajax 구분을 위해 헤더에서 검색할 키 명 입니다. */
	@Value("#{security['spring.ajax_header_key']}")
	private String SPRING_AJAX_HEADER_KEY;
	
	/** # Ajax 구분을 위해 헤더에서 검색할 키 값 입니다. */
	@Value("#{security['spring.ajax_header_value']}")
	private String SPRING_AJAX_HEADER_VALUE;
	
	private String loginFormPath;
	private String loginredirectname;
	
	public CustomAuthenticationEntryPoint(){
		super();
		loginFormPath = "/login";
		loginredirectname = "loginRedirect";
	}
	
	/**
	 * 다음 로직에 따라 인증되지 않은 유저에 대한 처리를 결정 합니다.
	 * 일반 요청이라면 요청자가 가고자 했던 URL 정보를 쿼리에 담아 로그인 페이지로 유도.
	 * Ajax 요청이라면 401 status 반환.
	 */
	@Override
	public void commence(
		HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
	)throws IOException, ServletException{
		// Ajax를 통해 들어온 것인지 파악한다.
		String ajaxHeader = request.getHeader(SPRING_AJAX_HEADER_KEY);
		
		if( !(ajaxHeader == null) && SPRING_AJAX_HEADER_VALUE.equals(ajaxHeader) ){
		// Ajax 요청이라면 401 status 반환
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}else{
		// 일반적인 요청이라면, 요청자가 가고자 했던 URL 정보를 쿼리에 담아 로그인 페이지로 유도
			// UrlUtils.buildFullRequestUrl(HttpServletRequest) 현재 요청 경로의 url을 구하는 메서드.
			String redirectUrl = UrlUtils.buildFullRequestUrl(request);
			String encoded = response.encodeRedirectURL(redirectUrl);
			response.sendRedirect(request.getContextPath() + loginFormPath + "?" + loginredirectname + "=" + encoded);
		}
	}
	
	public void setLoginFormPath(String loginPage){
		this.loginFormPath = loginPage;
	}

	public String getLoginredirectname(){
		return loginredirectname;
	}

	public void setLoginredirectname(String loginredirectname){
		this.loginredirectname = loginredirectname;
	}
}
