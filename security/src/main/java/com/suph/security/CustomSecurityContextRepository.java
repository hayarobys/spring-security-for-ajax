package com.suph.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomSecurityContextRepository implements SecurityContextRepository{
	private static final Logger logger = LoggerFactory.getLogger(CustomSecurityContextRepository.class);
	
	/**
	 * 로그인 성공 시(인증 성공 시) 생성한 쿠키가 존재하면, 해당 쿠키로부터 Authentication 객체를 생성합니다.
	 * Authentication 객체가 생성되었다면 SecurityContext객체에 보관합니다.
	 */
	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder reqResHolder){
		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		String authValue = getAuthCookieValue(reqResHolder.getRequest());
		// String authValue = "admin,ROLE_ADMIN,ROLE_MANAGER,ROLE_USER";
		
		if(authValue != null){
			String[] values = authValue.split(",");
			String id = values[0];
			String name = values[1];
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			
			for(int i = 2; i < values.length; i++){
				authorities.add(new SimpleGrantedAuthority(values[i]));	// 무슨 검증도 안하지..;; support메서드가 있는걸로 알고있으니, 나중에 찾아보자
			}
			
			MemberInfo user = new MemberInfo(id, name, "", authorities);
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", authorities);
			ctx.setAuthentication(authentication);
		}
		
		return ctx;
	}
	
	/**
	 * "AUTH" 라는 이름의 쿠키값을 반환하는 메서드.
	 * 인코딩이 잘못 되었을 시 UnsupportedEncodingException 발생
	 * 
	 * @param request
	 * @return String
	 */
	// 현재는 사용자 권한을 쿠키에서 읽어오고 있지만, 추후엔 DB에서 읽어오도록 바꿔보자. 혹은 JWT를 쓰거나.
	private String getAuthCookieValue(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		
		if(cookies == null){
			return null;
		}
		
		for(Cookie cookie : cookies){
			// 다음번엔 하드코딩 하지 말고, SuccessHandler의 값을 참고하도록 리팩토링 하자
			if(cookie.getName().equals("AUTH")){
				try{
					return URLDecoder.decode(cookie.getValue(), "utf-8");
				}catch(UnsupportedEncodingException e){
					return null;
				}
			}
		}
		
		return null;
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response){
		
	}

	@Override
	public boolean containsContext(HttpServletRequest request){
		
		return false;
	}

}
