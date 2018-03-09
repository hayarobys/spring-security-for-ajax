package com.suph.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController{
	/**
	 * 로그인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/login/edit")
	// 모든 HTTP METHOD에서 forward되므로, 접근 메서드를 GET으로 한정짓지 말자.
	public String login() {
		return "login/login";
	}
}
