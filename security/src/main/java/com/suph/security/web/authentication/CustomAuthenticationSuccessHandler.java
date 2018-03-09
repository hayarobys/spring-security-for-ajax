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

package com.suph.security.web.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * 인증 성공 시의 동작을 정의한 클래스 입니다. 로그인 성공시 단 한 번만 호출됩니다.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	//private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	/** Ajax 구분을 위해 헤더에서 검색할 키 명 입니다. */
	@Value("#{security['spring.ajax_header_key']}")
	private String SPRING_AJAX_HEADER_KEY;
	
	/** # Ajax 구분을 위해 헤더에서 검색할 키 값 입니다. */
	@Value("#{security['spring.ajax_header_value']}")
	private String SPRING_AJAX_HEADER_VALUE;
	
	/**
	 * 인증 실패시 스프링 시큐리티에서 RequestCache에 요청 URL을 저장하고, 인증 프로세서를 처리합니다.
	 * 즉 requestChache엔 요청했던 URL정보가 세션 형태로 담겨있습니다. 
	 */
	private RequestCache requestCache = new HttpSessionRequestCache();
	/** 인증 성공시 이동시킬 경로가 저장된 파라미터 명 */
	private String targetUrlParameter;
	/** Referer나 targetUrl 혹은 sessionUrl들이 비어있을 경우 이동시킬 기본 url */
	private String defaultUrl;
	/** Referer값이 존재할 경우 Referer경로로 이동시킬지 여부 설정 */
	private boolean useReferer;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public CustomAuthenticationSuccessHandler(){
		targetUrlParameter = "";
		defaultUrl = "/";
		useReferer = false;
	}
	
	/**
	 * 인증 성공시 이동시킬 url이 저장될 파라미터명을 반환합니다.
	 * @return
	 */
	public String getTargetUrlParameter(){
		return targetUrlParameter;
	}

	/**
	 * 인증 성공시 이동시킬 url을 저장할 파라미터명을 설정합니다.
	 * <input type="hidden"> name="targetUrl" value="/main" /> 또는
	 * http://localhost:8080/contextPath/~~?targetUrl="/main" 식으로 사용할 파라미터 명을 지정합니다.
	 * @param targetUrlParameter
	 */
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
	
	/**
	 * 로그인 성공 시 호출되는 메서드.
	 * 인증 성공 시 내부전략에 따라 요청자를 특정 페이지로 이동시킵니다.
	 * 전략은 decideRedirectStrategy 메소드를 참고하십시오.
	 */
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
		
		int intRedirectStrategy = decideRedirectStrategy(request, response);
		switch(intRedirectStrategy){
		case 1:
			useAjax(request, response);
			break;
		case 2:
			useTargetUrl(request, response);
			break;
		case 3:
			useSessionUrl(request, response);
			break;
		case 4:
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
	 * 인증 성공후 어떤 URL로 redirect 할지를 결정한다
	 * 판단 기준은 Ajax요청일 경우 200 status를 반환하는 것을 1순위
	 * ajax가 아닐 경우 targetUrlParameter 값을 읽은 URL이 존재할 경우 그것을 2순위
	 * 2순위 URL이 없을 경우 Spring Security가 세션에 저장한 URL을 3순위
	 * 3순위 URL이 없을 경우 Request의 REFERER를 사용하고 그 REFERER URL이 존재할 경우 그 URL을 4순위
	 * 4순위 URL이 없을 경우 Default URL을 5순위로 한다
	 * @param request
	 * @param response
	 * @return 1 : ajax 요청이므로 200 status 반환
	 *         2 : targetUrlParameter 값을 읽은 URL
	 *         3 : Session에 저장되어 있는 URL
	 *         4 : referer 헤더에 있는 url
	 *         0 : default url
	 */
	private int decideRedirectStrategy(HttpServletRequest request, HttpServletResponse response){
		int result = 0;
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		// ajax 요청인지 확인
		String ajaxHeader = request.getHeader(SPRING_AJAX_HEADER_KEY);
		if( !(ajaxHeader == null) && SPRING_AJAX_HEADER_VALUE.equals(ajaxHeader) ){
			result = 1;
			
		}else if(!"".equals(targetUrlParameter)){
			String targetUrl = request.getParameter(targetUrlParameter);
			if(StringUtils.hasText(targetUrl)){
				result = 2;
				
			}
		}else{
			if(savedRequest != null){
				result = 3;
				
			}else{
				String refererUrl = request.getHeader("REFERER");
				if(useReferer && StringUtils.hasText(refererUrl)){
					result = 4;
					
				}else{
					result = 0;
					
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 요청자를 targetUrlParameter에 적힌 url로 이동시킵니다.
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void useTargetUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		//if(savedRequest != null){
		//	requestCache.removeRequest(request, response);
		//}
		
		String targetUrl = request.getParameter(targetUrlParameter);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	/**
	 * 요청자를 sessionUrl에 저장된 값으로 이동시킵니다.
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl = savedRequest.getRedirectUrl();
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	/**
	 * 요청 헤더에 Referer값이 있을 경우 요청자를 이전 페이지로 이동시킵니다.
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void useRefererUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String targetUrl = request.getHeader("REFERER");
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	/**
	 * 요청자를 기본 설정된 페이지로 이동시킵니다. (미 지정시 기본값은 메인페이지)
	 * targetUrlParameter, sessionUrl, Referer값들이 비어있을 경우 호출됩니다.
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void useDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		redirectStrategy.sendRedirect(request, response, defaultUrl);
	}
	
	/**
	 * Ajax 요청이고 인증이 성공했으므로, 200 status를 반환 합니다.
	 */
	private void useAjax(HttpServletRequest request, HttpServletResponse reponse){
		// 아무런 처리를 해줄 필요가 없습니다.
		// response에 특별히 status를 설정하지 않는 경우에는 자연히 200 status를 반환하게 되니까요.
	}
}































