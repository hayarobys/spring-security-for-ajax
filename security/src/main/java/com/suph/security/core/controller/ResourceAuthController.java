package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.service.ResourceService;

@Controller
public class ResourceAuthController{
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * URL 관리 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping("/resource-auth/edit")
	public String urlControll(){
		return "url-controll";
	}
	
	/**
	 * 특정 url 접근에 필요한 권한 목록을 조회합니다.
	 * @param authNo
	 * @return
	 */
	@RequestMapping(value="/resource/{resourceNo}/auth", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> authList(@PathVariable int authNo){
		return resourceService.getAuthListByResourceNo(authNo);
	}
	
	/**
	 * 특정 리소스의 접근에 필요한 권한을 재지정 합니다.
	 * @return
	 */
	@RequestMapping(value="/resource/{resourceNo}/auth", method=RequestMethod.PATCH) // CSRF필터로 인해 임시GET을 사용해서 테스트 할 예정
	public @ResponseBody Map<String, Object> changeResourceAuth(){
		return null;
	}
}
