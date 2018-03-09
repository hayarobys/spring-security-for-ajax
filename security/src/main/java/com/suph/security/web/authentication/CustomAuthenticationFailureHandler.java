package com.suph.security.web.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 인증 실패시(로그인 실패시의) 동작을 정의한 클래스 입니다.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{
	
	protected final static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
	/** Ajax 구분을 위해 헤더에서 검색할 키 명 입니다. */
	@Value("#{security['spring.ajax_header_key']}")
	private String SPRING_AJAX_HEADER_KEY;
	
	/** # Ajax 구분을 위해 헤더에서 검색할 키 값 입니다. */
	@Value("#{security['spring.ajax_header_value']}")
	private String SPRING_AJAX_HEADER_VALUE;
	
	/** 로그인 시도한 id값이 들어오는 input 태그 name */
	private String loginidname;
	/** 로그인 시도한 password 값이 들어오는 input 태그 name */
	private String loginpasswdname;
//	/** 로그인 성공시 redirect 할 URL이 지정되어 있는 input 태그 name */
//	private String loginredirectname;
	/** 로그인 실패 사유를 request의 Attribute에 저장할 때 사용될 key 값 */
	private String exceptionmsgname;
	/** 로그인 실패시 보여줄 URL(로그인 화면) */
	private String defaultFailureUrl;
	
	public CustomAuthenticationFailureHandler(){
		this.loginidname = "j_username";
		this.loginpasswdname = "j_password";
//		this.loginredirectname = "loginRedirect";
		this.exceptionmsgname = "securityexceptionmsg";
		this.defaultFailureUrl = "/login.do";
	}
	
	/**
	 * 로그인 시도한 id값이 저장된 input태그의 name값을(id가 저장된 파라미터 명) 반환합니다.
	 * @return
	 */
	public String getLoginidname(){
		return loginidname;
	}

	/**
	 * 로그인 시도시 id값을 불러올 input태그의 name명(id 파라미터 명)을 지정합니다.
	 * @param loginidname
	 */
	public void setLoginidname(String loginidname){
		this.loginidname = loginidname;
	}

	/**
	 * 로그인 시도한 비밀번호값이 저장된 input태그의 name명을(패스워드 파라미터 명) 반환합니다.
	 * @return
	 */
	public String getLoginpasswdname(){
		return loginpasswdname;
	}

	/**
	 * 로그인 시도시 비밀번호 값을 불러올 input태그의 name명(password 파라미터 명)을 지정합니다.
	 * @param loginpasswdname
	 */
	public void setLoginpasswdname(String loginpasswdname){
		this.loginpasswdname = loginpasswdname;
	}
//
//	/**
//	 * 로그인 성공시 이동할 경로를 저장할 input태그의 name명을 반환 합니다.
//	 * @return
//	 */
//	public String getLoginredirectname(){
//		return loginredirectname;
//	}
//
//	/**
//	 * 로그인 성공시 이동시킬 경로를 저장할 input태그의 name명을 지정 합니다.
//	 * @param loginredirectname
//	 */
//	public void setLoginredirectname(String loginredirectname){
//		this.loginredirectname = loginredirectname;
//	}

	/**
	 * 로그인 실패 사유를 저장할 때 사용할 파라미터 명을 반환합니다.
	 * @return
	 */
	public String getExceptionmsgname(){
		return exceptionmsgname;
	}

	/**
	 * 로그인 실패 사유를 저장할 때 사용할 파라미터 명을 지정합니다.
	 * @param exceptionmsgname
	 */
	public void setExceptionmsgname(String exceptionmsgname){
		this.exceptionmsgname = exceptionmsgname;
	}

	/**
	 * 로그인 실패시 이동할 기본 경로를 반환합니다.
	 * @return
	 */
	public String getDefaultFailureUrl(){
		return defaultFailureUrl;
	}

	/**
	 * 로그인 실패시 이동시킬 기본 경로를 지정합니다.
	 * @param defaultFailureUrl
	 */
	public void setDefaultFailureUrl(String defaultFailureUrl){
		this.defaultFailureUrl = defaultFailureUrl;
	}

	/**
	 * 로그인 실패 시 호출되는 메소드. 로그인 실패시(인증 실패시) 어떤 동작을 수행할지 정의합니다.
	 * 인자로 오는 AuthenticationException에 어떤 이유로 실패했는지에 대한 정보가 담겨있습니다.
	 * defaultFailureUrl로 실패 사유, 인증 시도한 id, password 정보를 담아 이동시킵니다.
	 */
	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
	) throws IOException, ServletException{
		// ajax 요청인지 확인
		String ajaxHeader = request.getHeader(SPRING_AJAX_HEADER_KEY);
		
		if( !(ajaxHeader == null) && SPRING_AJAX_HEADER_VALUE.equals(ajaxHeader) ){
		// Ajax 요청이라면, 인증에 실패했으므로 401 status 반환
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}else{
		// 일반 요청이라면
			// Request 객체의 Attribute에 사용자가 실패시 입력했던 로그인 ID와 비밀번호를 저장해두어 로그인 페이지에서 이를 접근하도록 한다.
			String loginid = request.getParameter(loginidname);
			String loginpasswd = request.getParameter(loginpasswdname);
			
			request.setAttribute(loginidname, loginid);
			request.setAttribute(loginpasswdname, loginpasswd);
			
			// Request 객체의 Attribute에 예외 메시지 저장
			request.setAttribute(exceptionmsgname, exception.getMessage());
			request.getRequestDispatcher(defaultFailureUrl).forward(request, response);	// redirect하면 request영역을 공유할 수 없다. forward해주자.
			// forward함으로써 클라이언트가 보낸 request에 담긴 객체를 defaultFailureUrl페이지에서 다시 불러쓸 수 있다.
			// 다만 클라이언트의 주소창 값은 직전에 요청한 주소로 변경된다. 여기선 http://localhost:8443/security/login_check로 변경
			// forward하면 요청당시의 HTTP Method를 그대로 가지고 가야하는 문제또한 생긴다. 로그인 페이지를 GET/POST/DELETE/PATCH 모두로 접근 가능하게 해줘야 하므로
			// Restful에서 빗겨난다.
		}
	}
}



















