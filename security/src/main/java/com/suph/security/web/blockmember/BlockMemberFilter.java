package com.suph.security.web.blockmember;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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

import com.suph.security.core.dto.BlockInfoDTO;
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
			
			// class LinkedHashMap<K, V> extedns HashMap<K, V> implements Map<K, V>
			List<BlockInfoDTO> blockInfo = memberInfo.getBlockInfo();
			LOGGER.debug("필터에서 읽은 차단 정보: {}", blockInfo);
			
			// 이 계정에 현재 또는 미래에 대한 차단 정보가 있다면, 그중 현재 차단 기간이라는 정보가 있는지 조회합니다.
			if(		blockInfo != null
				&&	blockInfo.size() > 0
				//&&	blockInfo.getBlockExpireDate().after( new Date() )
			){
				Date currentTime = new Date();
				LinkedHashMap<String, Object> blockInfoObj = null;
				Long blockStartDateLong = null;
				Date blockStartDate = null;
				Long blockExpireDateLong = null;
				Date blockExpireDate = null;
				
				for(Object obj : blockInfo){
					blockInfoObj		= (LinkedHashMap<String, Object>)obj;
					
					blockStartDateLong	= (Long)blockInfoObj.get("blockStartDate");
					blockStartDate		= new Date(blockStartDateLong);
					
					blockExpireDateLong	= (Long)blockInfoObj.get("blockExpireDate");
					blockExpireDate		= new Date(blockExpireDateLong);
					
					String blockCause	= (String)blockInfoObj.get("blockCause");
					
					if(		blockStartDate.before(currentTime)
						&&	blockExpireDate.after(currentTime)
					){
						// 현재 차단 기간에 있는 계정 이라면 로그아웃 처리
						blockMemberHandler.onBlockMember(
								request, response,
								new BlockMemberException("차단된 계정입니다.", memberInfo)
						);
						
						break;
					}
				} // end of for(){}
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
