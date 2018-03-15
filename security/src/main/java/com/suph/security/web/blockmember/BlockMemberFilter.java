package com.suph.security.web.blockmember;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.suph.security.core.dto.BlockMemberDTO;
import com.suph.security.core.userdetails.MemberInfo;
import com.suph.security.web.toolonglogin.TooLongLoginBlockFilter;

/**
 * 1. 로그인 할 때마다 차단 목록 테이블을 조회
 * 2. 차단 계정일시, 만료일자를 JWT에 기록하여 발급
 * 3. 요청이 올때마다, 계정 차단 필터에서 만료일자 조회하여 안내 메시지 응답. 동시에 해당 계정의 로그인 토큰 제거.
 */
public class BlockMemberFilter implements Filter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TooLongLoginBlockFilter.class);
	
	private BlockMemberHandler blockMemberHandler;
	
	public BlockMemberFilter(){
		super();
		blockMemberHandler = new SimpleBlockMemberHandler();
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException{
		
	}

	@Override
	public void doFilter(
			ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException{
		
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try{
			Assert.notNull(authentication, "Authentication에 null이 들어가선 안됩니다.");
		}catch(IllegalArgumentException iae){
			chain.doFilter(request, response);
			return;
		}
		
		Object principal = authentication.getPrincipal();
		if(principal instanceof MemberInfo){
			MemberInfo memberInfo = (MemberInfo)principal;
			BlockMemberDTO blockInfo = memberInfo.getBlockInfo();
			//LOGGER.debug("필터에서 읽은 차단 정보: {}", blockInfo);
			if(		blockInfo != null
				&&	blockInfo.getBlockExpireDate().after( new Date() )
			){
				// 차단된 계정이라면 로그아웃 처리
				blockMemberHandler.onBlockMember(
						request, response,
						new BlockMemberException("차단된 계정입니다.", memberInfo)
				);
			}
		}
		
		// 통과
		chain.doFilter(request, response);
	}

	@Override
	public void destroy(){
		
	}
	
	public void setBlockMemberHandler(BlockMemberHandler blockMemberHandler){
		this.blockMemberHandler = blockMemberHandler;
	}
}
