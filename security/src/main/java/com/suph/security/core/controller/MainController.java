package com.suph.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController{
	/**
	 * 메인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String main() {
		return "main/main";
	}
}
