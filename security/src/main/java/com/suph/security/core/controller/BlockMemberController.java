package com.suph.security.core.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dto.BlockMemberDTO;
import com.suph.security.core.dto.SearchBlockMemberDTO;
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
	 * 현시간 기준, 특정 계정의 차단 종료일이 남아 있을 경우, 그 차단 정보를 조회합니다.
	 * @param memNo
	 * @return
	 */
	@RequestMapping(value="/block-member/{memNo}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBlockMemberByMemNoAndExpireDateIsAfterTheCurrentDate(@PathVariable(required=true) Integer memNo){
		return blockMemberService.getBlockMemberByMemNoAndExpireDateIsAfterTheCurrentDate(memNo);
	}
	
	/**
	 * 현시간 기준, 검색 조건에 따른 차단 이력 조회
	 * @return
	 */
	@RequestMapping(value="/block-member", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBlockMember(
			@RequestParam(value="searchType",		required=false)	String searchType,
			@RequestParam(value="searchKeyword",	required=false)	String searchKeyword,
			@RequestParam(value="searchStartDate",	required=false)	String blockStartDate,
			@RequestParam(value="searchExpireDate",	required=false)	String blockExpireDate,
			@RequestParam(value="searchTime[]",		required=false)	List<String> searchTime
	){
		//logger.debug("searchTime[]: {}", searchTime);
		if(searchKeyword != null){
			logger.debug("검색 키워드 자료형: {}", searchKeyword.getClass().getName());	
		}
		
		SearchBlockMemberDTO searchBlockMemberDTO = new SearchBlockMemberDTO();
		
		searchBlockMemberDTO.setSearchType(searchType);
		searchBlockMemberDTO.setSearchKeyword(searchKeyword);
		searchBlockMemberDTO.setSearchTime(searchTime);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		
		Date startDate = null;
		Date expireDate = null;
		
		if(StringUtils.hasText(blockStartDate)){
			try{
				startDate = format.parse(blockStartDate);				
				searchBlockMemberDTO.setBlockStartDate(startDate);
			}catch(ParseException e){ e.printStackTrace(); }
		}
		
		if(StringUtils.hasText(blockExpireDate)){
			try{
				expireDate = format.parse(blockExpireDate);
				searchBlockMemberDTO.setBlockExpireDate(expireDate);
			}catch(ParseException e){ e.printStackTrace(); }
		}
						
		return blockMemberService.getBlockMember(searchBlockMemberDTO);
	}
	
	/**
	 * 차단 계정 추가
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	@RequestMapping(value="/block-member", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> postBlockMember(
			@RequestBody Map<String, String> blockData
	){
		BlockMemberDTO blockMemberDTO = new BlockMemberDTO();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		
		String memNo = blockData.get("memNo");
		String blockCause = blockData.get("blockCause");
		String blockStartDate = blockData.get("setStartDate");
		String blockExpireDate = blockData.get("setExpireDate");
		
		if(StringUtils.hasText(memNo)){
			try{
				blockMemberDTO.setMemNo( Integer.parseInt(memNo) );
			}catch(NumberFormatException nfe){ nfe.printStackTrace(); }
		}
		
		if(StringUtils.hasText(blockCause)){
			blockMemberDTO.setBlockCause(blockCause);
		}
		
		if(StringUtils.hasText(blockStartDate)){
			try{
				Date startDate = format.parse(blockStartDate);				
				blockMemberDTO.setBlockStartDate(startDate);
			}catch(ParseException e){ e.printStackTrace(); }
		}
		
		if(StringUtils.hasText(blockExpireDate)){
			try{
				Date expireDate = format.parse(blockExpireDate);
				blockMemberDTO.setBlockExpireDate(expireDate);
			}catch(ParseException e){ e.printStackTrace(); }
		}
		
		return blockMemberService.postBlockMember(blockMemberDTO);
	}
	
	/**
	 * 차단 계정 정보 수정
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	@RequestMapping(value="/block-member/{blockNo}", method=RequestMethod.PATCH)
	public @ResponseBody Map<String, Object> patchBlockMember(@PathVariable(required=true) Integer blockNo, @RequestBody BlockMemberDTO blockMemberDTO){
		return blockMemberService.patchBlockMember(blockNo, blockMemberDTO);
	}
	
	/**
	 * 차단 계정 정보 삭제
	 * @param memNo
	 * @return
	 */
	@RequestMapping(value="/block-member/{blockNo}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteBlockMember(@PathVariable(required=true) Integer blockNo){
		return blockMemberService.deleteBlockMember(blockNo);
	}
}











