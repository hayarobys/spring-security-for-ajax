package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dto.MemberDTO;
import com.suph.security.core.service.MemberService;

@Controller
public class MemberController{
	@Autowired
	private MemberService memberService;
		
	/**
	 * 계정 관리 페이지 이동
	 */
	@RequestMapping(value="/member/edit", method=RequestMethod.GET)
	public String getMemberEdit(){
		return "member/member";
	}
	
	/**
	 * 모든 계정 목록 반환
	 * @param memId
	 * @param memNicknm
	 */
	@RequestMapping(value="/member", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getMember(
			@RequestParam(name="pagenum", required=false, defaultValue="1") int pagenum,
			@RequestParam(name="pagesize", required=false, defaultValue="20") int pagesize,
			@RequestParam(name="memId", required=false) String memId,
			@RequestParam(name="memNicknm", required=false) String memNicknm
	){
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setPagenum(pagenum);
		memberDTO.setPagesize(pagesize);
		memberDTO.setMemId(memId);
		memberDTO.setMemNicknm(memNicknm);
		
		return memberService.getMember(memberDTO);
	}
	
	/**
	 * 모든 계정 목록 중 해당 페이지 만큼 반환
	 */
	@RequestMapping(value="/member/page/{pageNo}", method=RequestMethod.GET)
	public Map<String, Object> getMemberByPage(){
		return null;
	}
	
	/**
	 * 특정 계정을 수정합니다
	 * @param memNo 수정할 계정의 일련 번호
	 * @param memberDTO 수정할 정보
	 * @return
	 */
	@RequestMapping(value="/member/{memNo}", method=RequestMethod.PATCH)
	public @ResponseBody Map<String, Object> patchMember(
			@PathVariable(required=true) Integer memNo,
			@RequestBody MemberDTO memberDTO
	){
		return memberService.patchMember(memNo, memberDTO);
	}
	
	/**
	 * 특정 계정 삭제(관리자 전용)
	 */
	@RequestMapping(value="/member/{memNo}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteMember(@PathVariable(required=true) Integer memNo){
		return memberService.deleteMember(memNo);
	}
	
	/**
	 * 회원가입 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/member/registration", method=RequestMethod.GET)
	public String register(){
		return "register";
	}
	
	/**
	 * 계정 아이디 중복 검사
	 */
	@RequestMapping(value="/check/member/id", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> postMemberIdDuplicateCheck(@RequestBody MemberDTO memberDTO){
		return memberService.postMemberIdDuplicateCheck(memberDTO.getMemId());
	}
	
	/**
	 * 계정을 등록합니다
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/member", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> postMember(@RequestBody MemberDTO memberDTO){
		return memberService.postMember(memberDTO);
	}
	
	/**
	 * 나의 계정 삭제 / 회원 탈퇴(로그인 당사자 전용)
	 */
	@RequestMapping(value="/member/me", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteMyAccount(){
		return null;
	}
}
