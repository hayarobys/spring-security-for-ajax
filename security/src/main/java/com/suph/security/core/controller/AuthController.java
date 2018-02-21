package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.service.AuthService;

@Controller
public class AuthController{
	@Autowired
	private AuthService authService;
	
	/**
	 * 권한 관리 페이지로 이동
	 */
	@RequestMapping(value="/auth/edit", method=RequestMethod.GET)
	public String getAuthEdit(){
		return "";
	}
	
	/**
	 * 모든 권한 목록을 조회합니다.
	 * @return
	 */
	@RequestMapping(value="/auth", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAuth(){
		return authService.getAuthList();
	}
	
	/**
	 * 특정 권한을 삭제 합니다.
	 */
	@RequestMapping(value="/auth/{authNo}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteAuthByAuthNo(@PathVariable int authNo){
		return null;
	}
	
}
