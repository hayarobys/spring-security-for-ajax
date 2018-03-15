package com.suph.security.web.blockmember;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BlockMemberHandler{
	/**
	 * 차단된 계정의 접속일 경우, 호출되는 메소드 입니다.
	 * 이동해야할 경로, 로그아웃 처리, 메시지 등을 정의합니다.
	 * @param req
	 * @param res
	 * @param exception
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void onBlockMember(
			HttpServletRequest req,
			HttpServletResponse res,
			BlockMemberException exception
	) throws IOException, ServletException;
}
