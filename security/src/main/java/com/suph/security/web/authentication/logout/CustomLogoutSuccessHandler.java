package com.suph.security.web.authentication.logout;

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

/**
 * 로그아웃 요청시 처리동작을 정의한 클래스 입니다.
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	/** SecurityContext 생성을 위한 JWT가 저장될 쿠키의 key명 입니다. */
	@Value("#{security['spring_security_context_key']}")
	private String SPRING_SECURITY_CONTEXT_KEY;
	
	/** 클라이언트에서 사용할 수 있게 닉네임 정보를 담아둘 JWT닉네임 쿠키의 key명 입니다. */
	@Value("#{security['jwt_nickname_cookie_key']}")
	private String JWT_NICKNAME_COOKIE_KEY;
	
	/** Ajax 구분을 위해 헤더에서 검색할 키 명 입니다. */
	@Value("#{security['spring.ajax_header_key']}")
	private String SPRING_AJAX_HEADER_KEY;
	
	/** # Ajax 구분을 위해 헤더에서 검색할 키 값 입니다. */
	@Value("#{security['spring.ajax_header_value']}")
	private String SPRING_AJAX_HEADER_VALUE;
	
	/**
	 * 로그아웃 성공시 "SPRING_SECURITY_CONTEXT_KEY"라는 이름의 쿠키를 제거합니다.
	 * 그 후, 컨텍스트 루트 경로로 리디렉트 합니다.
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException{
		logout(request, response, authentication, SPRING_SECURITY_CONTEXT_KEY, JWT_NICKNAME_COOKIE_KEY);
		
		// ajax 요청인지 확인
		String ajaxHeader = request.getHeader(SPRING_AJAX_HEADER_KEY);
		if( !(ajaxHeader == null) && SPRING_AJAX_HEADER_VALUE.equals(ajaxHeader) ){
		// Ajax 요청이라면  로그아웃 성공했으므로 200 status 반환
			// 아무런 처리를 해줄 필요가 없습니다.
			// response에 특별히 status를 설정하지 않는 경우에는 자연히 200 status를 반환하게 되니까요.
		}else{
		// 일반 요청이라면 로그아웃 성공했으므로 /main으로 이동
			response.sendRedirect(request.getContextPath() + "/main");
		}
	}
	
	/**
	 * 로그아웃 처리시 요청자의 관련 쿠키를 제거하는 용도로 호출됩니다.
	 * 전달받은 request경로에서 생성한 cookieName명의 쿠키를 제거하도록 response에 설정합니다.
	 * @param request
	 * @param response
	 * @param authentication 제거할 쿠키 명을 지정합니다. 여러개의 값을 지정할 수 있습니다.
	 * @param cookiesToClear
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String... cookiesToClear){
		List<String> cookiesToClearList = Arrays.asList(cookiesToClear);
		
		for (String cookieName : cookiesToClearList) {
	        Cookie cookie = new Cookie(cookieName, null);
	        cookie.setPath("/");
	        cookie.setMaxAge(0);
	        response.addCookie(cookie);
		}
    }
}
