package com.suph.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyInfoController{
		
	/**
	 * 나의 정보 페이지 이동
	 */
	@RequestMapping(value="/my-info/edit", method=RequestMethod.GET)
	public String getMemberEdit(){
		return "my-info/my-info";
	}
}
