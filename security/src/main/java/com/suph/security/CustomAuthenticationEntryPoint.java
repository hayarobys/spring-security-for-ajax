package com.suph.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
	private String loginFormPath;
	private String loginredirectname;
	
	public CustomAuthenticationEntryPoint(){
		super();
		loginFormPath = "/login";
		loginredirectname = "loginRedirect";
	}
	
	@Override
	public void commence(
		HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
	)throws IOException, ServletException{
		
		// UrlUtils.buildFullRequestUrl(HttpServletRequest) 현재 요청 경로의 url을 구하는 메서드.
		String redirectUrl = UrlUtils.buildFullRequestUrl(request);
		String encoded = response.encodeRedirectURL(redirectUrl);
		response.sendRedirect(request.getContextPath() + loginFormPath + "?" + loginredirectname + "=" + encoded);
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
