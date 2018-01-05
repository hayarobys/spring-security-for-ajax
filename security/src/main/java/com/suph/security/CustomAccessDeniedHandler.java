package com.suph.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	private String errorPage;
	
	@Override
	public void handle( HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException ) throws IOException, ServletException{
		
		// Ajax를 통해 들어온 것인지 파악한다.
		String ajaxHeader = request.getHeader("X-Ajax-call");
		String result = "";
		
		// 권한이 없을시 뜨는 예외 처리 장소이므로 응답 상태 코드를 403(접근금지/권한없음)으로 설정한다.
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding("UTF-8");
		
		if(ajaxHeader == null){
		// null로 받은 경우는 X-Ajax-call 헤더 변수가 없다는 의미이기 때문에 Ajax가 아닌 일반적인 방법으로 접근했음을 의미한다.
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();
			
			if(principal instanceof UserDetails){
				String username = ((UserDetails)principal).getUsername();
				
				request.setAttribute("username", username);
			}
			
			request.setAttribute("errormsg", accessDeniedException);
			request.getRequestDispatcher(errorPage).forward(request, response);	// xml등에서 미리 설정한 에러페이지로 이동
			
		}else{
		// 그외 요청(Ajax라거나...)에서 권한이 불충분 한 것이라면
			if("true".equals(ajaxHeader)){	// true로 값을 받았다는 것은 ajax로 접근했음을 의미한다.
				result = "{\"result\":\"fail\",\"message\":\"" + accessDeniedException.getMessage() + "\"}";
				
			}else{	// 헤더값 설정을 잘못했다고 간주하고 그에 따른 에러메시지를 보낸다.
				result = "{\"result\":\"fail\",\"message\":\"Access Denied(Header Value Mismatch)\"}";
			}
			
			response.getWriter().print(result);
			response.getWriter().flush();
		}
	}
	
	public void setErrorPage(String errorPage){
		if((errorPage != null) && !errorPage.startsWith("/")){
			throw new IllegalArgumentException("errorPage must begin with '\'");
		}
		this.errorPage = errorPage;
	}
}
