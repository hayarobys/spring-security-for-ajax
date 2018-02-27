package com.suph.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController{
	
	/**
	 * 관리자 전용 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(){
		return "admin/admin";
	}
}
