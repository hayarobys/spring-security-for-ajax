package com.suph.security.web.blockmember;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.suph.security.core.dto.BlockInfoDTO;
import com.suph.security.core.userdetails.MemberInfo;

public class SimpleBlockMemberHandler implements BlockMemberHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBlockMemberHandler.class);
	
	private static final String LOGIN_ID_NAME = "j_username";
	private static final String EXCEPTION_MSG_NAME = "securityexceptionmsg";
	private static final String BLOCK_INFO_NAME = "blockinfo";
	private static final String DEFAULT_FAILURE_URL = "/block-info";
	
	/** 차단한 계정의 id값을 넣어줄 request Attribute명 */
	private String loginidname = LOGIN_ID_NAME;
	/** 차단 계정의 접근 시 로그인 실패 이유를 request의 Attribute에 저장할 때 사용될 key 값 */
	private String exceptionmsgname = EXCEPTION_MSG_NAME;
	/** 차단 정보를 request의 Attribute에 저장할 때 사용될 key 값 */
	private String blockinfoname = BLOCK_INFO_NAME;
	/** 차단 계정의 접근 후 차단 안내를 보여줄 페이지의 URL(로그인 화면) */
	private String defaultFailureUrl = DEFAULT_FAILURE_URL;
	
	@Override
	public void onBlockMember(
			HttpServletRequest request, HttpServletResponse response,
			BlockMemberException exception
	) throws IOException, ServletException{
		SecurityContextHolder.clearContext();
		MemberInfo memberInfo = exception.getMemberInfo();
		List<BlockInfoDTO> blockInfo = memberInfo.getBlockInfo();
		/*
		LOGGER.info("이 요청자(계정번호: {})는 '{} ~ {}'의 기간 동안 차단된 계정 입니다.", new Object[]{
			memberInfo.getUsername(), blockInfo.getBlockStartDate(), blockInfo.getBlockExpireDate()	
		});
		*/
		
		LOGGER.info("이 요청자({})는 차단된 계정 입니다: {}", new Object[]{
				memberInfo.getUsername(), blockInfo	
		});
		
		// 현재 접근 요청자의 ID를 request 영역에 저장해두어 로그인 페이지에서 이를 꺼내쓰도록 한다.
		String username = memberInfo.getUsername();
		String loginid = memberInfo.getId();
		String name = memberInfo.getName();
		
		request.setAttribute("username", username); // 계정 일련 번호
		request.setAttribute(loginidname, loginid);	// 계정 아이디
		request.setAttribute("name", name);			// 계정 닉네임
		request.setAttribute(exceptionmsgname, exception.getMessage());
		request.setAttribute(blockinfoname, blockInfo);
		request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
	}
	
	/** 차단된 계정의 id값을 넣어줄 request Attribute명을 저장/변경 합니다. */
	public void setLoginidname(String loginidname){
		this.loginidname = loginidname;
	}
	
	/** 차단계정의 접근 시 로그인 실패 이유를 request의 Attribute에 저장할 때 사용될 key 값을 저장/변경 합니다. */
	public void setExceptionmsgname(String exceptionmsgname){
		this.exceptionmsgname = exceptionmsgname;
	}
	
	/** 차단계정의 시작일, 만료일, 사유 정보를 request의 Attribute에 저장할 때 사용될 key 값을 저장/변경 합니다. */
	public void setBlockinfoname(String blockinfoname){
		this.blockinfoname = blockinfoname;
	}
	
	/** 차단계정의 접근 후 재로그인 유도 시 보여줄 URL(로그인 화면)을 저장/변경 합니다. */
	public void setDefaultFailureUrl(String defaultFailureUrl){
		this.defaultFailureUrl = defaultFailureUrl;
	}
}
