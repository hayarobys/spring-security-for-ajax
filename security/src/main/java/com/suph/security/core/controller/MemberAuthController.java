package com.suph.security.core.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dto.MemberAuthDTO;
import com.suph.security.core.service.MemberAuthService;

@Controller
public class MemberAuthController{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MemberAuthService memberAuthService;
	
	/**
	 * 계정-권한 관리 페이지 이동
	 */
	@RequestMapping(value="/member-auth/edit", method=RequestMethod.GET)
	public String getMemberAuthEdit(){
		return "member-auth/member-auth";
	}
	
	/**
	 * 특정 계정이 보유한 권한 목록 반환
	 */
	@RequestMapping(value="/member/{memNo}/auth", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAuthOfMember(@PathVariable(required=true) Integer memNo){
		return memberAuthService.getAuthOfMemberByMemNo(memNo);
	}
	
	/**
	 * 특정 계정이 보유한 권한 목록 중 해당 페이지 만큼 반환
	 */
	@RequestMapping(value="/member/{memNo}/auth/page/{pageNo}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAuthOfMemberByPageNo(
			@PathVariable(value="memNo", required=true) Integer memNo,
			@PathVariable(value="pageNo") Integer pageNo
	){
		return null;
	}
	
	/**
	 * 툭정 계정이 보유한 권한 목록 업데이트
	 */
	@RequestMapping(value="/member/{memNo}/auth", method=RequestMethod.PATCH)
	public @ResponseBody Map<String, Object> patchAuthOfMember(
			@PathVariable(value="memNo", required=true) Integer memNo,
			@RequestBody MemberAuthDTO memberAuthDTO
	){
		logger.debug("memNo: {}, memberAuthDTO: {}", memNo, memberAuthDTO);
		return memberAuthService.patchAuthOfMemberByMemNo(memNo, memberAuthDTO);
	}
}
