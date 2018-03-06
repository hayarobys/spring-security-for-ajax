package com.suph.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController{
	/**
	 * 로그인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "login/login";
	}
}
