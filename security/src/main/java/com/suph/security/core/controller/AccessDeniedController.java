package com.suph.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccessDeniedController{
	/**
	 * 접근 거부 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/access-denied", method=RequestMethod.GET)
	public String accessDenied(){
		return "access-denied/access-denied";
	}
	
	/**
	 * 차단된 계정의 접근시 이 URL로 이동시킵니다.
	 * 해당 페이지에서 차단 사유, 차단 시작 일, 차단 만료 일 등의 정보를 출력합니다.
	 * @return
	 */
	@RequestMapping(value="/block-info")
	public String getBlockInfoPage(){
		return "access-denied/block-info";
	}
}
