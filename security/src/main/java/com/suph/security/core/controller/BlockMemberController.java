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

import com.suph.security.core.dao.ResourceDAO;
import com.suph.security.core.dto.BlockMemberDTO;
import com.suph.security.core.service.BlockMemberService;

@Controller
public class BlockMemberController{
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BlockMemberService blockMemberService;
	
	/**
	 * 차단 계정 관리 페이지로 이동
	 * @return
	 * 
	 */
	@RequestMapping(value="/block-member/edit", method=RequestMethod.GET)
	public String getBlockMemberControllPage(){
		return "block-member/block-member";
	}
	
	/**
	 * 특정 계정의 차단 이력을 조회합니다.
	 * @param memNo
	 * @return
	 */
	@RequestMapping(value="/block-member/{memNo}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBlockMemberByMemNo(@PathVariable(required=true) Integer memNo){
		return blockMemberService.getBlockMemberByMemNo(memNo);
	}
	
	/**
	 * 모든 차단 계정 목록 조회
	 * @return
	 */
	@RequestMapping(value="/block-member", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBlockMember(){
		return blockMemberService.getBlockMember();
	}
	
	/**
	 * 차단 계정 추가
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	@RequestMapping(value="/block-member/{memNo}", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> postBlockMember(@RequestBody BlockMemberDTO blockMemberDTO){
		return blockMemberService.postBlockMember(blockMemberDTO);
	}
	
	/**
	 * 차단 계정 정보 수정
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	@RequestMapping(value="/block-member/{memNo}", method=RequestMethod.PATCH)
	public @ResponseBody Map<String, Object> patchBlockMember(@PathVariable(required=true) Integer memNo, @RequestBody BlockMemberDTO blockMemberDTO){
		return blockMemberService.patchBlockMember(memNo, blockMemberDTO);
	}
	
	/**
	 * 차단 계정 정보 삭제
	 * @param memNo
	 * @return
	 */
	@RequestMapping(value="/block-member/{memNo}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteBlockMember(@PathVariable(required=true) Integer memNo){
		return blockMemberService.deleteBlockMember(memNo);
	}
}











