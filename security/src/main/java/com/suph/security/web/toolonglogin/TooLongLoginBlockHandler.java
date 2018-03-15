package com.suph.security.web.toolonglogin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TooLongLoginBlockHandler{
	/**
	 * 마지막 로그인 으로부터 오랜 시간 경과 후, 호출되는 메소드 입니다.
	 * 이동해야할 경로, 메시지 등을 정의합니다.
	 * 
	 * @param response
	 * @param request
	 * @param exception
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void onTooLongLoginBlock(HttpServletResponse response, HttpServletRequest request, TooLongLoginException exception)
			throws IOException, ServletException;
}
