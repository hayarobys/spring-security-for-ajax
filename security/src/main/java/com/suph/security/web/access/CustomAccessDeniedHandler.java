package com.suph.security.web.access;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.suph.security.core.userdetails.MemberInfo;

import io.jsonwebtoken.lang.Assert;

/**
 * 인증 상태의 요청이 인가에 실패 했을시 처리 동작을 정의한 클래스 입니다.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	
	/** Ajax 구분을 위해 헤더에서 검색할 키 명 입니다. */
	@Value("#{security['spring.ajax_header_key']}")
	private String SPRING_AJAX_HEADER_KEY;
	
	/** # Ajax 구분을 위해 헤더에서 검색할 키 값 입니다. */
	@Value("#{security['spring.ajax_header_value']}")
	private String SPRING_AJAX_HEADER_VALUE;
	
	/** 권한 부족으로 접근 거부시 보여줄 페이지 URL */
	private String errorPage;
	
	/**
	 * 요청자가 요청한 리소스 접근에 권한부족으로 거부당했을때 처리할 기능을 정의합니다.
	 * Ajax 요청이라면 {"result":"fail","message":"{원인}"} 이라는 json 데이터를 응답합니다.
	 * 일반적인 get/post http 요청이라면 응답 헤더에 "errormsg" 라는 키값으로 에러 원인을 저장하고, xml등에서 미리 설정해둔 접근 거부 페이지로 이동시킵니다.
	 * 
	 * 이 메소드는 ajax요청인지 일반 요청인지 구분하기 위해 요청 헤더에서 "X-Ajax-call" 이라는 키값을 검사합니다.
	 * 해당 키값이 없다면 일반 요청으로 인식하고, 해당 키값이 존재하면서 그 내용이 "true"라면 ajax로 인식합니다.
	 * 반면 "X-Ajax-call"이라는 키가 존재하지만 그 내용이 "true" 이외의 문자열을 포함하고 있다면 비정상적인 Ajax 요청으로 인식하고
	 * {"result":"fail","message":"Access Denied(Header Value Mismatch)"}라는 json 데이터를 전송합니다.
	 * 
	 * 그러니 이 메소드의 정상적인 동작을 바란다면 모든 Ajax 요청시 ("X-Ajax-call", "true") 를 헤더에 포함하십시오.
	 */
	@Override
	public void handle( HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException ) throws IOException, ServletException{
		// Ajax를 통해 들어온 것인지 파악한다.
		String ajaxHeader = request.getHeader(SPRING_AJAX_HEADER_KEY);
		String result = "";
		
		// 권한이 없을시 뜨는 예외 처리 장소이므로 응답 상태 코드를 403(접근금지/권한없음)으로 설정한다.
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding("UTF-8");
		
		if(ajaxHeader == null){
		// null로 받은 경우는 X-Ajax-call 헤더 변수가 없다는 의미이기 때문에 Ajax가 아닌 일반적인 방법으로 접근했음을 의미한다.
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			try{
				Assert.notNull(auth);
				
				Object principal = auth.getPrincipal();
				
				if(principal instanceof MemberInfo){
					MemberInfo memberInfo = (MemberInfo)principal;
					
					String username = memberInfo.getUsername();
					String id = memberInfo.getId();
					String name = memberInfo.getName();
					Collection<? extends GrantedAuthority> authorities = memberInfo.getAuthorities();
					
					request.setAttribute("username", username);
					request.setAttribute("id", id);
					request.setAttribute("name", name);
					request.setAttribute("authorities", authorities);
				}
			}catch(IllegalArgumentException iae){
				LOGGER.debug("유효한 Authentication을 찾지 못했습니다. 접근 거부 페이지를 응답합니다.");
			}
			
			request.setAttribute("errormsg", accessDeniedException);
			request.getRequestDispatcher(errorPage).forward(request, response);	// xml등에서 미리 설정한 에러페이지로 이동
			
		}else{
		// 그외 요청(Ajax라거나...)에서 권한이 불충분 한 것이라면
			LOGGER.debug("이 Ajax요청이 거부되었습니다. 권한이 불충분 하거나, 올바른 CSRF토큰이 없을 수 있습니다.\n메시지: {}", accessDeniedException.getMessage());
			if(SPRING_AJAX_HEADER_VALUE.equals(ajaxHeader)){	// true로 값을 받았다는 것은 ajax로 접근했음을 의미한다.
				result = "{\"result\":\"fail\",\"message\":\"" + accessDeniedException.getMessage() + "\"}";
				
			}else{	// 헤더값 설정을 잘못했다고 간주하고 그에 따른 에러메시지를 보낸다.
				result = "{\"result\":\"fail\",\"message\":\"Access Denied(Header Value Mismatch)\"}";
			}
			
			response.getWriter().print(result);
			response.getWriter().flush();
		}
	}
	
	/**
	 * 권한 불충분등의 이유로 접근을 거부당했을때 보여줄 페이지 경로를 지정합니다.
	 * @param errorPage	페이지 경로
	 */
	public void setErrorPage(String errorPage){
		if((errorPage != null) && !errorPage.startsWith("/")){
			throw new IllegalArgumentException("errorPage must begin with '\'");
		}
		this.errorPage = errorPage;
	}
}
