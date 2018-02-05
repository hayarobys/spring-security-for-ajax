package com.suph.security.web.toolonglogin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.suph.security.core.userdetails.MemberInfo;

/**
 * 장시간 로그인으로 재로그인을 유도하는 동작이 정의된 핸들러
 * @author NB-0267
 *
 */
public class SimpleTooLongLoginBlockHandler implements TooLongLoginBlockHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTooLongLoginBlockHandler.class);
	
	private static final String LOGIN_ID_NAME = "j_username";
	private static final String EXCEPTION_MSG_NAME = "securityexceptionmsg";
	private static final String DEFAULT_FAILURE_URL = "/login.do";
	
	/** 장시간 로그인 한 계정의 id값을 넣어줄 request Attribute명 */
	private String loginidname = LOGIN_ID_NAME;
	/** 장시간 로그인 시 재로그인 유도 사유를 request의 Attribute에 저장할 때 사용될 key 값 */
	private String exceptionmsgname = EXCEPTION_MSG_NAME;
	/** 장시간 로그인 후 재로그인 유도 시 보여줄 URL(로그인 화면) */
	private String defaultFailureUrl = DEFAULT_FAILURE_URL;
	
	@Override
	public void onTooLongLoginBlock(
			HttpServletResponse response, HttpServletRequest request, TooLongLoginException exception
	) throws IOException, ServletException{
		//LOGGER.debug("SecurityContextHolder의 Authentication이 null값으로 초기화 됩니다.");
		SecurityContextHolder.clearContext();
		MemberInfo memberInfo = exception.getMemberInfo();
		
		// "마지막 로그인으로부터 오랜 시간이 경과 했습니다. 재로그인 해주세요."
		LOGGER.info(exception.getMessage());
		
		// 현재 접근 요청자의 ID를 request영역에 저장해두어 로그인 페이지에서 이를 접근하도록 한다.
		String loginid = memberInfo.getId();
		
		request.setAttribute(loginidname, loginid);
		request.setAttribute(exceptionmsgname, exception.getMessage());
		request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
	}
	
	/** 장시간 로그인 한 계정의 id값을 넣어줄 request Attribute명을 저장/변경 합니다. */
	public void setLoginidname(String loginidname){
		this.loginidname = loginidname;
	}
	
	/** 장시간 로그인 시 재로그인 유도 사유를 request의 Attribute에 저장할 때 사용될 key 값을 저장/변경 합니다. */
	public void setExceptionmsgname(String exceptionmsgname){
		this.exceptionmsgname = exceptionmsgname;
	}
	
	/** 장시간 로그인 후 재로그인 유도 시 보여줄 URL(로그인 화면)을 저장/변경 합니다. */
	public void setDefaultFailureUrl(String defaultFailureUrl){
		this.defaultFailureUrl = defaultFailureUrl;
	}
}
