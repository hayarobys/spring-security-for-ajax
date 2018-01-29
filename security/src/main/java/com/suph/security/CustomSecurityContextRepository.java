package com.suph.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

public class CustomSecurityContextRepository implements SecurityContextRepository{
	private static final Logger logger = LoggerFactory.getLogger(CustomSecurityContextRepository.class);
	
	/** SecurityContext 생성을 위한 JWT가 저장될 쿠키의 key명 입니다. */
	@Value("#{security['spring_security_context_key']}")
	private String SPRING_SECURITY_CONTEXT_KEY;
	
	/** 시큐리티 쿠키의 유효 시간 입니다.(단위: ms) */
	@Value("#{security['security_cookie.exp_time_per_sec']}")
	private int SECURITY_COOKIE_EXP_TIME;
	
	/** JWT 토큰의 유효 시간 입니다.(단위: ms) */
	@Value("#{security['jwt.exp_time_per_ms']}")
	private int TOKEN_EXP_TIME;
	
	/** 로그인 유효 시간 입니다.(단위: ms) */
	@Value("#{security['login.exp_time_per_ms']}")
	private int LOGIN_EXP_TIME;
	
	/** JWT 암/복호화에 사용되는 대칭키 입니다. */
	@Value("#{security['jwt.secret']}")
	private String SECRET;
	
	/** JWT payload에 저장할 계정 일련 번호 Claim명 입니다. */
	@Value("#{security['jwt.claim.no']}")
	private String JWTNo;
	
	/** JWT payload에 저장할 아이디 Claim명 입니다. */
	@Value("#{security['jwt.claim.id']}")
	private String JWTId;
	
	/** JWT payload에 저장할 닉네임 Claim명 입니다. */
	@Value("#{security['jwt.claim.name']}")
	private String JWTName;
	
	/** JWT payload에 저장할 권한 Claim명 입니다. */
	@Value("#{security['jwt.claim.authorities']}")
	private String JWTAuthorities;
	
	/**	(미인증된)기본 내용과 동일한지를 확인하는 데 사용되는 SecurityContext 객체 */
	private final Object contextObject = SecurityContextHolder.createEmptyContext();
	
	/** SecurityContext 생성을 위한 JWT가 저장될 쿠키의 key명 입니다. */
	private String springSecurityContextKey = SPRING_SECURITY_CONTEXT_KEY;
	
	private boolean disableUrlRewriting = false;
	
	/** 인증 토큰을 평가하는데 사용되는 객체 */
	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	
	/**
	 * 요청(인증, 인가)이 들어올 시 쿠키로부터 JWT토큰을 읽어들여 SecurityContext생성
	 * 
	 * @param requestResponseHolder 쿠키가 담겨있는 요청 객체
	 * @return 
	 */
	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder){
		HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        
        Cookie jwtCookie = getCookieFromRequest(request, SPRING_SECURITY_CONTEXT_KEY);
        if(jwtCookie == null){
        	logger.debug("요청으로부터 " + SPRING_SECURITY_CONTEXT_KEY + "에 해당하는 jwtCookie를 발견하지 못했습니다. 새로운 Cookie를 생성합니다.");
        	jwtCookie = generateContextClearCookie(SPRING_SECURITY_CONTEXT_KEY);
        }
        
        SecurityContext context = readSecurityContextFromJWT(jwtCookie.getValue());
        
        if (context == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No SecurityContext was available from the HttpServletRequest: " + request +". " +
                        "A new one will be created.");
            }
            context = generateNewContext();
        }
        
        SaveToCookieResponseWrapper wrappedResponse = new SaveToCookieResponseWrapper(response, request, context);
        requestResponseHolder.setResponse(wrappedResponse);
        
        return context;
	}
	
	/**
	 * 쿠키에 JWT토큰을 저장합니다.
	 */
	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response){
		SaveToCookieResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, SaveToCookieResponseWrapper.class);
		
		if(responseWrapper == null){
			throw new IllegalStateException("Cannot invoke saveContext on response " + response + ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
		}
		
		// 필터체인에서 sendError() 또는 sendRedirect()를 호출한 경우,
		// saveContex()는 ResponseWrapper에 의해 이미 호출되었을 수 있습니다.
		// 이렇게 하면 요청 당 한 번만 호출됩니다.
		if( !responseWrapper.isContextSaved() ){
			responseWrapper.saveContext(context);
		}
	}
	
	/**
	 * 해당 Request가 SecurityContex생성에 필요한 JWT쿠키를 지니고 있는지 확인합니다.
	 */
	@Override
	public boolean containsContext(HttpServletRequest request){
		Cookie cookie = getCookieFromRequest(request, springSecurityContextKey);
		
		if(cookie == null){
			return false;
		}
		
		return cookie.getValue() != null;
	}
	
	/**
	 * 주어진 JWT로 SecurityContext를 생성합니다.
	 * @param cookieArr
	 * @return
	 */
	private SecurityContext readSecurityContextFromJWT(String jwt){
		
		if(!StringUtils.hasText(jwt)){
			logger.debug("No JWT currently exists");
			
			return null;
		}
		
		Object contextFromJWT = generateSecurityContextFromJWT(jwt);
		
		if(contextFromJWT == null){
			logger.debug("JWT returned null object");
			return null;
		}
		
		// We now have the security context object from the session.
		if(!(contextFromJWT instanceof SecurityContext)){
			logger.warn(springSecurityContextKey + " did not contain a SecurityContext but contained: '"
					+ contextFromJWT + "'; are you improperly modifying the HttpServletRequest or Cookie directly?"
					+ "(you should always use SecurityContextHolder)"
			);
			
			return null;
		}
		
		logger.debug("Obtain a valid SecurityContext from " + springSecurityContextKey + ": '" + contextFromJWT + "'");
		
		// Everything OK. The only non-null return from this method.
		return (SecurityContext) contextFromJWT;
	}
	
	/**
	 * 주어진 JWT로 부터 필요한 값을 찾아내 SecurityContext를 생성합니다.
	 * @param cookieArr
	 * @param authenticationKey
	 * @return
	 */
	private SecurityContext generateSecurityContextFromJWT(String jwt){
		// Get MemberInfo infomation(id, name, authorities) from JWT claims
		Claims claims = null;
		
		try{
			claims = JWTUtility.getClaims(jwt, SECRET);
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("JWT파싱에 실패했습니다. {}", e.getMessage());
			return null;
		}
		
		String no = (String)JWTUtility.getClaim(claims, JWTNo);
		if( !StringUtils.hasText(no) ){
			logger.debug("JWT에 유효한 no이 없습니다.");
			return null;
		}
		
		String id = (String)JWTUtility.getClaim(claims, JWTId);
		if( !StringUtils.hasText(id) ){
			logger.debug("JWT에 유효한  id가 없습니다.");
			return null;
		}
		
		String name = (String)JWTUtility.getClaim(claims, JWTName);
		if( !StringUtils.hasText(name) ){
			logger.debug("JWT에 유효한 name이 없습니다.");
			return null;
		}
		
		String authorities = (String)JWTUtility.getClaim(claims, JWTAuthorities);
		if( !StringUtils.hasText(authorities) ){
			logger.debug("JWT에 유효한 권한이 없습니다.");
			return null;
		}
		
		Date issuedAt = claims.getIssuedAt();	// JWT 생성일 
		long currentTime = System.currentTimeMillis();	// 현재 시간
		long diff = getDiffFromCurrTimeInMs(issuedAt); // 시간차
		if(diff >= LOGIN_EXP_TIME){
			logger.debug("로그인 상태를 오랜시간 유지했습니다. 재로그인 해주세요. 마지막 로그인 시점: {}, 시간차: {}분", issuedAt.toString(), diff/1000/60);
			return null;
		}
		
		String[] authorityArray = authorities.split(",");
		List<GrantedAuthority> roleList = new ArrayList<GrantedAuthority>();
		for(String role : authorityArray){
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
			roleList.add(grantedAuthority);
		}
		
		MemberInfo memberInfo = new MemberInfo(Integer.parseInt(no), id, "[PROTECTED]", name, roleList, issuedAt);
		
		// MemberInfo -> (Authentication)UsernamePasswordAuthenticationToken
		Authentication authentication = new UsernamePasswordAuthenticationToken(memberInfo, "[PROTECTED]", roleList);
		
		// Authentication -> SecurityContext
		SecurityContext securityContext = generateNewContext(); 
		securityContext.setAuthentication(authentication);
		
		return securityContext;
	}
	
	/**
	 * 주어진 날짜로부터 현재 시간과의 차를 구합니다. (단위: ms)
	 * @param pastDate
	 * @return
	 */
	public static long getDiffFromCurrTimeInMs(Date pastDate){
		long currentTime = System.currentTimeMillis();	// 현재 시간
		long diff = Math.subtractExact(currentTime, pastDate.getTime()); // 시간차
		logger.debug("시간차는 {} 입니다.", diff);
		return diff;
	}
	
	/**
	 * HTTP 요청으로부터 특정 key의 cookie를 불러옵니다.
	 * @param request
	 * @param key
	 * @return
	 */
	public static Cookie getCookieFromRequest(HttpServletRequest request, String key){
		if(request == null || key == null){
			return null;
		}
		
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if( key.equals(cookie.getName()) ){
					return cookie;
				}
			}
		}
		
		logger.debug("요청으로부터 " + key + "에 해당하는 쿠키가 발견되지 않았습니다.");
		return null;
	}
	
	/**
	 * Authentication값이 비어있는 SecurityContext 구현체 생성
	 * SecurityContextHolder가 비어있을때 호출된다.
	 * @return
	 */
	protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }
	
	/**
	 * 스프링 시큐리티에서 생성한 SPRING_SECURITY_CONTEXT_KEY에 매칭된 쿠키를 제거합니다.
	 * 제공받은 key값으로 비어있는 쿠키로 덮어쓰는 용도의 쿠키를 생성하여 반환합니다.
	 * @return
	 */
	public static Cookie generateContextClearCookie(String key){
		Cookie cookie = new Cookie(key, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		
		return cookie;
	}
	
	/**
	 * 스프링 시큐리티에서 최종적으로 생성한 SecurityContext를 JWT Cookie로 변환하는데 사용됩니다.
	 * 단 JWT생성일은 요청당시 JWT의 내용을 그대로 유지합니다. 요청당시 토큰이나 생성일이 없었다면 현재시간으로 생성 합니다.
	 * 이는 로그인 유효 시간을 검사하는데 사용됩니다.
	 * @param context
	 * @return
	 */
	protected Cookie generateJWTContextCookie(SecurityContext context){
		Map<String, Object> contextClaims = new HashMap<String, Object>();
		
		Authentication authentication = context.getAuthentication();
		Object principal = authentication.getPrincipal();
		Date issuedAt = null;
		if(principal instanceof MemberInfo){
			MemberInfo memberInfo = (MemberInfo)principal;
			String no = memberInfo.getUsername();	// PK값 반환(이 경우 계정 일련 번호)
			String id = memberInfo.getId();
			String name = memberInfo.getName();
			issuedAt = memberInfo.getIssuedAt();
			
			Collection<? extends GrantedAuthority> authorityList = memberInfo.getAuthorities();
			StringBuilder authBuilder = new StringBuilder();
			for(GrantedAuthority auth : authorityList){
				if(authBuilder.length() > 0){
					authBuilder.append(',');
				}
				authBuilder.append(auth.getAuthority());
			}
			String authorities = authBuilder.toString();
			
			contextClaims.put(JWTNo, no);
			contextClaims.put(JWTId, id);
			contextClaims.put(JWTName, name);
			contextClaims.put(JWTAuthorities, authorities);
			
		}else if(principal instanceof String){
			contextClaims.put(JWTName, (String)principal);
		}
		
		// 토큰 최초 생성일이 존재하지 않다면 현재 시간으로 재생성
		if(issuedAt == null){
			issuedAt = new Date();
		}
		
		String jwtContext = JWTUtility.createJWT(
				issuedAt,
				new Date(System.currentTimeMillis() + TOKEN_EXP_TIME),
				"www.security.org",
				contextClaims,
				SECRET
		);
		Cookie jwtContextCookie = new Cookie(SPRING_SECURITY_CONTEXT_KEY, jwtContext);
		jwtContextCookie.setMaxAge(SECURITY_COOKIE_EXP_TIME);
		jwtContextCookie.setPath("/");
		jwtContextCookie.setSecure(true);
		jwtContextCookie.setHttpOnly(true);
		
		return jwtContextCookie;
	}
	
	/**
	 * AuthenticationTrustResolver 타입의 인증 토큰을 평가할 객체를 지정합니다.
	 * @param trustResolver
	 */
	public void setTrustResolver(AuthenticationTrustResolver trustResolver){
		Assert.notNull(trustResolver, "trustResolver cannot be null");
		this.trustResolver = trustResolver;
	}
	
	final class SaveToCookieResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper{
		private final HttpServletRequest request;
        private final SecurityContext contextBeforeExecution;
        private final Authentication authBeforeExecution;
        
        SaveToCookieResponseWrapper(
        		HttpServletResponse response,
        		HttpServletRequest request,
        		SecurityContext context
        ){
        	super(response, disableUrlRewriting);
            this.request = request;
            this.contextBeforeExecution = context;
            this.authBeforeExecution = context.getAuthentication();
        }

        /**
         * 전달받은 SecurityContext를 응답 쿠키에 저장합니다.
         * 보안 필터 체인을 돌고난 후에 최종적으로 수행되며, 수행 전 SecurityContext에서 변동시 생긴 경우에만 저장합니다.
         * 만약 인증상태로 요청이 왔으나, 보안 필터 체인을 돌고난 후 미인증 상태로 변경되었다면 응답 객체에 기존 쿠키를 제거하도록 설정합니다.
         */
		@Override
		protected void saveContext(SecurityContext context){
			final Authentication authentication = context.getAuthentication();
			HttpServletResponse response = (HttpServletResponse)getResponse();
			
			if(authentication == null || trustResolver.isAnonymous(authentication)){
			// 보안필터체인을 돌고난 후에 SecurityContext가 비었거나 비회원 권한인 상태라면 어떤 처리도 하지 않습니다.
				logger.debug("SecurityContext is empty or contents are anonymous - The Server will send a cookie clear response.");
				
				if(!contextObject.equals(contextBeforeExecution)){
				// 다만 보안필터체인을 돌기전에 생성한 SecurityContext가 인증 상태었다면 기존 SecurityContext 쿠키를 제거하라고 응답합니다.
					Cookie clearCookie = generateContextClearCookie(SPRING_SECURITY_CONTEXT_KEY);
					response.addCookie(clearCookie);
				}
				
				return;
			}
			
			/*
			if(contextChanged(context)){
			// 보안필터체인을 돌고난 후의 SecurityContext가 이전과 다르다면 쿠키에 저장합니다.
				// 인증 정보에 변동이 발생한 경우에만 토큰 유효시간을 연장합니다.
				Cookie jwtContextCookie = generateJWTContextCookie(context);
				jwtContextCookie.setSecure(true);
				response.addCookie(jwtContextCookie);
				
				logger.debug("다음의 SecurityContext를 쿠키에 저장합니다: {}", context);
			}
			*/
			
			// 보안필터체인을 돌고난 후 SecurityContext가 변했든 말든 여전히 인증상태라면 JWT를 재발급 합니다.
			// 이렇게 하면 토큰의 유효시간이 연장되는 효과가 있습니다. 활동 중인 유저의 로그인 상태를 유지하는 것입니다.
			Cookie jwtContextCookie = generateJWTContextCookie(context);
			jwtContextCookie.setSecure(true);
			response.addCookie(jwtContextCookie);
			
			logger.debug("다음의 SecurityContext를 쿠키에 저장합니다: {}", context);
			
		}
		
		/**
		 * 전달받은 SecurityContext가 이 객체 생성 당시의 SecurityContext와 다른지 여부를 반환합니다.
		 * @param context	비교할 SecurityContext
		 * @return
		 */
		private boolean contextChanged(SecurityContext context){
			return context != contextBeforeExecution || context.getAuthentication() != authBeforeExecution;
		}
	}	// end of SaveToCookieResponseWrapper{}
}	// end of CustomSecurityContextRepository{}
























