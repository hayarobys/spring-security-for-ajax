package com.suph.security.web.blockmember;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 1. 로그인 할 때마다 차단 목록 테이블을 조회
 * 2. 차단 계정일시, 만료일자를 JWT에 기록하여 발급
 * 3. 요청이 올때마다, 계정 차단 필터에서 만료일자 조회하여 안내 메시지 응답. 동시에 해당 계정의 로그인 토큰 제거.
 */
public class BlockMember implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException{
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(){
		// TODO Auto-generated method stub

	}

}
