package com.suph.security.web.toolonglogin;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.suph.security.core.userdetails.MemberInfo;

/**
 * 마지막 로그인 으로 부터의 경과 시간을 계산하고, 미리 설정한 max값을 초과할 경우 재로그인 페이지로 유도합니다.
 * @author NB-0267
 *
 */
public class TooLongLoginBlockFilter implements Filter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TooLongLoginBlockFilter.class);
	
	/** 로그인 유효 시간 입니다.(단위: ms) */
	@Value("#{security['login.exp_time_per_ms']}")
	private long LOGIN_EXP_TIME;
	
	@Value("#{security['jwt.exp_time_per_ms']}")
	private long JWT_EXP_TIME;
	
	/** 장시간 로그인 시 수행할 동작을 정의한 핸들러 변수 */
	private TooLongLoginBlockHandler tooLongLoginBlockHandler = null;
	
	public TooLongLoginBlockFilter(){
		super();
		tooLongLoginBlockHandler = new SimpleTooLongLoginBlockHandler();
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException{
		
		
	} // end of init()
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException{
		
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try{
			Assert.notNull(authentication, "Authentication에 null이 들어가선 안됩니다.");
		}catch(IllegalArgumentException iae){
			//LOGGER.debug("유효한 Authentication을 찾지 못했습니다. 다음 필터로 넘어갑니다.");
			chain.doFilter(request, response);
			return;
		}
		
		Object principal = authentication.getPrincipal();
		if(principal instanceof MemberInfo){
			MemberInfo memberInfo = (MemberInfo)principal;
			Date issuedAt = memberInfo.getIssuedAt();	// 최초 로그인 날짜
			long diff = getDiffFromCurrTimeInMs(issuedAt); // 시간차
			
			LOGGER.info("이 요청자(계정번호: {})는 마지막 로그인 시간 으로부터 {}분 지났습니다. (최대시간: {}분, 토큰 유효 시간: {}분)", new Object[]{memberInfo.getNo(), diff/1000/60, LOGIN_EXP_TIME/1000/60, JWT_EXP_TIME/1000/60});
			
			if(diff >= LOGIN_EXP_TIME){
				// 너무 오랜시간 로그인 했으므로 재로그인 유도
				tooLongLoginBlockHandler.onTooLongLoginBlock(
						response, request,
						new TooLongLoginException("마지막 로그인으로부터 오랜 시간이 경과 했습니다. 재로그인 해주세요.", memberInfo)
				);	
			}
		}
		
		// 통과
		chain.doFilter(request, response);
	} // end of doFilter()
	
	@Override
	public void destroy(){
		
		
	} // end of destroy()
	
	/**
	 * 장시간 로그인 한 계정에 대한 처리동작이 정의된 클래스를 저장/변경 합니다. 
	 * @param tooLongLoginBlockHandler
	 */
	public void setTooLongLoginBlockHandler(TooLongLoginBlockHandler tooLongLoginBlockHandler){
		this.tooLongLoginBlockHandler = tooLongLoginBlockHandler;
	}
	
	/**
	 * 주어진 날짜로부터 현재 시간과의 차를 구합니다. (단위: ms)
	 * @param pastDate
	 * @return
	 */
	public static long getDiffFromCurrTimeInMs(Date pastDate){
		long currentTime = System.currentTimeMillis();	// 현재 시간
		long diff = Math.subtractExact(currentTime, pastDate.getTime()); // 시간차
		
		return diff;
	}
} // end of TooLongLoginBlockFilter{}
