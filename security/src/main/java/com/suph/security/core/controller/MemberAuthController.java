package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MemberAuthController{
	/**
	 * 계정-권한 관리 페이지 이동
	 */
	@RequestMapping(value="/member-auth/edit", method=RequestMethod.GET)
	public String getMemberAuthEdit(){
		return "";
	}
	
	/**
	 * 특정 계정이 보유한 권한 목록 반환
	 */
	@RequestMapping(value="/member/{memberNo}/auth", method=RequestMethod.GET)
	public Map<String, Object> getMemberAuthByMember(){
		return null;
	}
	
	/**
	 * 특정 계정이 보유한 권한 목록 중 해당 페이지 만큼 반환
	 */
	@RequestMapping(value="/member/{memberNo}/auth/page/{pageNo}")
	public Map<String, Object> getMemberAuthByMemberAndPage(){
		return null;
	}
	
	/**
	 * 툭정 계정이 보유한 권한 목록 업데이트
	 */
	@RequestMapping(value="/memeber/{memberNo}/auth", method=RequestMethod.PATCH)
	public Map<String, Object> patchMemberAuthByMember(){
		return null;
	}
}
