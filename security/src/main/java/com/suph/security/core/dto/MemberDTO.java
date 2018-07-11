package com.suph.security.core.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class MemberDTO extends PaginationRequest{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** MEM_NO 계정 일련 번호 PK */
	private Integer memNo;
	/** MEM_ID 계정 아이디 */
	private String memId;
	/** MEM_PASSWORD 계정 비밀번호 */
	private transient String memPassword;
	/** MEM_NICKNM 계정 사용자의 이름 또는 별명 */
	private String memNicknm;
	/** 계정 상태 (활성, 휴면, 차단, 탈퇴, ...) */
	private String memState;
	
	/**
	 * LAST_LOGIN_DATE 마지막 로그인 일자
	 */
	private java.util.Date lastLoginDate;
	
	/**
	 * 계정 일련 번호를 반환합니다.
	 * @return memNo 반환
	 */
	public Integer getMemNo(){
		return memNo;
	}
	
	/**
	 * 계정 일련 번호를 변경/저장 합니다.
	 * @param memNo
	 */
	public void setMemNo(Integer memNo){
		this.memNo = memNo;
	}
	
	/**
	 * 계정 ID를 반환합니다.
	 * @return memId 반환
	 */
	public String getMemId(){
		return memId;
	}
	
	/**
	 * 계정 ID를 변경/저장 합니다.
	 * @param id
	 */
	public void setMemId(String memId){
		this.memId = StringUtils.hasText(memId) ? memId.trim() : memId;
	}
	
	/**
	 * 계정의 비밀번호를 반환합니다.
	 * @return memPassword 반환
	 */
	public String getMemPassword(){
		return memPassword;
	}
	
	/**
	 * 계정 비밀번호를 변경/저장 합니다.
	 * @param memPassword
	 */
	public void setMemPassword(String memPassword){
		this.memPassword = memPassword.trim();
	}
	
	/**
	 * 계정 사용자의 이름을 반환합니다.
	 * @return memNicknm 반환
	 */
	public String getMemNicknm(){
		return memNicknm;
	}
	
	/**
	 * 계정 사용자의 이름을 변경/저장 합니다.
	 * @param memNicknm
	 */
	public void setMemNicknm(String memNicknm){
		this.memNicknm = StringUtils.hasText(memNicknm) ? memNicknm.trim() : memNicknm;
	}
	
	public String getMemState(){
		return memState;
	}
	
	public void setMemState(String memState){
		this.memState = memState.trim();
	}

	public java.util.Date getLastLoginDate(){
		return lastLoginDate;
	}
	
	public void setLastLoginDate(java.util.Date lastLoginDate){
		this.lastLoginDate = lastLoginDate;
	}

	@Override
	public String toString(){
		return "MemberDTO [memNo=" + memNo + ", memId=" + memId + ", memPassword=" + "[PROTECT]" + ", memNicknm="
				+ memNicknm + ", memState=" + memState + ", lastLoginDate=" + lastLoginDate + "]";
	}
}
