package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dao.MemberDAO;
import com.suph.security.core.dto.MemberDTO;
import com.suph.security.core.service.MemberService;

@Controller
public class MemberController{
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private MemberDAO memberDAO;
	
	/**
	 * 계정 관리 페이지 이동
	 */
	@RequestMapping(value="/member/edit", method=RequestMethod.GET)
	public String getMemberEdit(){
		return "member/member";
	}
	
	/**
	 * 모든 계정 목록 반환
	 */
	@RequestMapping(value="/member", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getMember(){
		return memberService.getMember();
	}
	
	/**
	 * 모든 계정 목록 중 해당 페이지 만큼 반환
	 */
	@RequestMapping(value="/member/page/{pageNo}", method=RequestMethod.GET)
	public Map<String, Object> getMemberByPage(){
		return null;
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
	 * 회원가입 동작을 수행하고 메인페이지로 이동합니다.
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/member", method=RequestMethod.POST)	// rest로 변경 예정
	public String register(MemberDTO vo){
		
		vo.setMemPassword(
				passwordEncoder.encode( vo.getMemPassword() )
		);
		
		int result = memberDAO.insertMemberVO(vo);
		System.out.println(result > 0 ? "등록 성공" : "등록 실패");
		
		return "redirect:/main";
	}
	
	/**
	 * 특정 계정 삭제 / 회원 탈퇴
	 */
	@RequestMapping(value="/member/{memberNo}", method=RequestMethod.DELETE)
	public Map<String, Object> deleteMember(){
		return null;
	}
}
