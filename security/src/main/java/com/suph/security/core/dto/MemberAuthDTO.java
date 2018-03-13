package com.suph.security.core.dto;

import java.util.List;

/**
 * 계정-권한 정보를 담는데 사용합니다.
 */
public class MemberAuthDTO{
	/** MEM_NO: 계정 일련 번호 */
	private Integer memNo;
	/** 보유 권한 목록 */
	private List<Integer> authNoList;
	
	public Integer getMemNo(){
		return memNo;
	}
	
	public void setMemNo(Integer memNo){
		this.memNo = memNo;
	}

	public List<Integer> getAuthNoList(){
		return authNoList;
	}

	public void setAuthNoList(List<Integer> authNoList){
		this.authNoList = authNoList;
	}

	@Override
	public String toString(){
		return "MemberAuthDTO [memNo=" + memNo + ", authNoList=" + authNoList + "]";
	}
}
