/*
 * 로그인 성공시 처리할 동작을 정의한 클래스
 * Spring Security는 기본적으로 AuthenticationSuccessHandler를 구현한
 * org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler,
 * org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccess
 * 이 두가지를 제공하고 있다.
 * 
 * 저 기반으로 커스터마이징 하기 위해 한 번 봐두자.
 * 
 * 
 * 참고: http://zgundam.tistory.com/52?category=430446
 * ㄱ. 지정된 Request Parameter(loginRedirect)에 로그인 작업을 마친 뒤 redirect 할 URL을 지정했다면 이 URL로 redirect 하도록 한다.
 * ㄴ. 만약 지정된 Request Parameter에 지정된 URL이 없다면 세션에 저장된 URL로 redirect 하도록 한다.
 *     (Spring Security가 제공하는 RequestCache 인터페이스에 대한 이해가 필요)
 * ㄷ. 세션에 저장된 URL도 없다면 Request의 REFERER 헤더값을 읽어서 로그인 페이지를 방문하기 전 페이지의 URL을 읽어서 거기로 이동하도록 한다.
 *     (REFERER 기능 사용 여부는 설정 가능하도록 한다. 이 기능 설정을 해야 하는 이유는 밑에서 별도로 설명하도록 하겠다)
 * ㄹ. 위의 3가지 경우 모두 만족하는게 없으면 CustomAuthenticationSuccessHandler 클래스에 있는 defaultUrl 속성에 지정된 URL로  이동하도록 한다.
 */

package com.suph.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	private RequestCache requestCache = new HttpSessionRequestCache();
	private String targetUrlParameter;
	private String defaultUrl;
	private boolean useReferer;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public CustomAuthenticationSuccessHandler(){
		targetUrlParameter = "";
		defaultUrl = "/";
		useReferer = false;
	}
	
	public String getTargetUrlParameter(){
		return targetUrlParameter;
	}

	public void setTargetUrlParameter(String targetUrlParameter){
		this.targetUrlParameter = targetUrlParameter;
	}

	public String getDefaultUrl(){
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl){
		this.defaultUrl = defaultUrl;
	}

	public boolean isUseReferer(){
		return useReferer;
	}

	public void setUseReferer(boolean useReferer){
		this.useReferer = useReferer;
	}
	
	// 로그인 성공 시 호출되는 메서드
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException{
		/*
		// 유저명 가져오기
		Object principal = auth.getPrincipal();
		String name = "";
		if(principal != null && principal instanceof MemberInfo){
			name = ((MemberInfo)principal).getName();
			// 미인증 상태에선 "anonymousUser" 가 반환된다.
		}
		*/
		
		// 로그인에 단 한 번이라도 실패했을 경우 세션엔 마지막 실패 원인 메시지가 저장됩니다.
		// 이는 로그인 성공후에도 남아있기에, AuthenticationSuccessHandler를 구현한 클래스들에선 기본적으로 저 메시지를 지워주고 있습니다.
		clearAuthenticationAttributes(request);
		
		addAuthCookie(response, auth);
		
		int intRedirectStrategy = decideRedirectStrategy(request, response);
		switch(intRedirectStrategy){
		case 1:
			useTargetUrl(request, response);
			break;
		case 2:
			useSessionUrl(request, response);
			break;
		case 3:
			useRefererUrl(request, response);
			break;
		default:
			useDefaultUrl(request, response);
		}
		
	}
	
	/**
	 * 이 세션에서 "SPRING_SECURITY_LAST_EXCEPTION"으로 저장된 값을 제거합니다.
	 * 마지막 로그인 실패 문구를 제거하는 작업입니다.
	 * @param request
	 */
	private void clearAuthenticationAttributes(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		
		if(session == null){
			return;
		}
		
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
	/**
	 * 로그인 성공시 JWT 토큰을 생성합니다.
	 * 토큰에는 다음의 정보가 담깁니다. 권한, 닉네임, 아이디, 만료일자, 생성일자
	 * 
	 * @param response
	 * @param authentication
	 */
	public void addAuthCookie(HttpServletResponse response, Authentication authentication){
		// UsernamePasswordAuthenticationToken의 Object principal; 반환. userDetails를 구현한 MemberInfo 객체가 반환됨
		MemberInfo user = (MemberInfo)authentication.getPrincipal();
		String cookieValue = user.getId() + "," + user.getName();	// 로그인 성공한 유저의 아이디 반환

		if(authentication.getAuthorities() != null){
			for(GrantedAuthority auth : authentication.getAuthorities()){
				cookieValue += "," + auth.getAuthority();	// {AUTH : 아이디,닉네임,ROLE_ADMIN,ROLE_MANAGER} 식으로 쿠키에 넣을값 생성
			}
		}
		
		try{
			// 실제로는 쿠키값을 암호화해서 저장한다.(추후 JWT를 사용해 보자)
			Cookie cookie = new Cookie("AUTH", URLEncoder.encode(cookieValue, "utf-8"));
			cookie.setPath("/");
			response.addCookie(cookie);
		}catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 인증 성공후 어떤 URL로 redirect 할지를 결정한다
	 * 판단 기준은 targetUrlParameter 값을 읽은 URL이 존재할 경우 그것을 1순위
	 * 1순위 URL이 없을 경우 Spring Security가 세션에 저장한 URL을 2순위
	 * 2순위 URL이 없을 경우 Request의 REFERER를 사용하고 그 REFERER URL이 존재할 경우 그 URL을 3순위
	 * 3순위 URL이 없을 경우 Default URL을 4순위로 한다
	 * @param request
	 * @param response
	 * @return 1 : targetUrlParameter 값을 읽은 URL
	 *         2 : Session에 저장되어 있는 URL
	 *         3 : referer 헤더에 있는 url
	 *         0 : default url
	 */
	private int decideRedirectStrategy(HttpServletRequest request, HttpServletResponse response){
		int result = 0;
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if(!"".equals(targetUrlParameter)){
			String targetUrl = request.getParameter(targetUrlParameter);
			if(StringUtils.hasText(targetUrl)){
				result = 1;
			}
		}else{
			if(savedRequest != null){
				result = 2;
			}else{
				String refererUrl = request.getHeader("REFERER");
				if(useReferer && StringUtils.hasText(refererUrl)){
					result = 3;
				}else{
					result = 0;
				}
			}
		}
		
		return result;
	}
	
	private void useTargetUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if(savedRequest != null){
			requestCache.removeRequest(request, response);
		}
		
		String targetUrl = request.getParameter(targetUrlParameter);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl = savedRequest.getRedirectUrl();
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useRefererUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String targetUrl = request.getHeader("REFERER");
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		redirectStrategy.sendRedirect(request, response, defaultUrl);
	}
}































