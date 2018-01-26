package com.suph.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	/** 기존 SecurityContext 정보가 저장된 JWT 쿠키의 key명 입니다. */
	@Value("#{security['spring_security_context_key']}")
	private String SPRING_SECURITY_CONTEXT_KEY;
	
	/**
	 * 로그아웃 성공시 관련 쿠키를 제거합니다.
	 * 그 후, 컨텍스트 루트 경로로 리디렉트 합니다.
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException{
		logout(request, response, authentication, SPRING_SECURITY_CONTEXT_KEY);
		response.sendRedirect(request.getContextPath() + "/main");
	}
	
	/**
	 * 로그아웃 처리시 요청자의 관련 쿠키를 제거하는 용도로 호출됩니다.
	 * 전달받은 request경로에서 생성한 cookieName명의 쿠키를 제거하도록 response에 설정합니다.
	 * @param request
	 * @param response
	 * @param authentication
	 * @param cookiesToClear
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String... cookiesToClear){
		List<String> cookiesToClearList = Arrays.asList(cookiesToClear);
		
		for (String cookieName : cookiesToClearList){
	        Cookie cookie = new Cookie(cookieName, null);
	        String cookiePath = request.getContextPath();
	        
	        if(!StringUtils.hasLength(cookiePath)) {
	            cookiePath = "/";
	        }
	        
	        cookie.setPath(cookiePath);
	        cookie.setMaxAge(0);
	        cookie.setSecure(true);
	        cookie.setHttpOnly(true);
	        
	        response.addCookie(cookie);
		}
    }
}
