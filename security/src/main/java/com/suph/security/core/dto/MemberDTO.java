package com.suph.security.core.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberDTO{
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
	 * MySQL datetime > YYYY-MM-DD HH:MM:SS 
	 * Java ZonedDateTime >  2007-12-03T10:15:30+01:00[Europe/Paris]
	 */
	private ZonedDateTime lastLoginDate;
	
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
		this.memId = memId.trim();
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
		this.memNicknm = memNicknm.trim();
	}
	
	public String getMemState(){
		return memState;
	}
	
	public void setMemState(String memState){
		this.memState = memState.trim();
	}

	/**
	 * java.time.ZonedDateTime to java.time.ZonedDateTime
	 * @return
	 */
	public ZonedDateTime getLastLoginDateAsJavaTimeZonedDateTime(){
		return lastLoginDate;
	}
	
	/**
	 * java.time.ZonedDateTime to java.util.Date
	 * yyyy-mm-
	 * @return
	 */
	public java.util.Date getLastLoginDateAsJavaUtilDate(){
		if(lastLoginDate == null){
			return null;
		}
		
		java.util.Date date = java.sql.Date.from(
				lastLoginDate.toInstant()
		);
			
		return date;
	}
	
	/**
	 * 마지막 로그인 시간을 long타입의 밀리세컨트 형식으로 반환합니다.
	 * @return
	 */
	public Long getLastLoginDateAsMillis(){
		return getLastLoginDateAsJavaUtilDate().getTime();
	}
	
	/**
	 * java.time.ZonedDateTime to java.sql.Date
	 * toString() 호출시 시,분,초는 미출력 함에 유의
	 * @return
	 */
	public java.sql.Date getLastLoginDateAsJavaSqlDate(){
		if(lastLoginDate == null){
			return null;
		}
		return java.sql.Date.valueOf( lastLoginDate.toLocalDate() );
	}

	/**
	 * java.time.ZonedDateTime to java.time.ZonedDateTime
	 * @param lastLoginDate
	 */
	public void setLastLoginDateAsJavaTimeZonedDateTime(java.time.ZonedDateTime lastLoginDate){
		this.lastLoginDate = lastLoginDate;
	}
	
	/**
	 * java.util.Date to java.time.ZonedDateTime
	 * @param lastLoginDate
	 */
	public void setLastLoginDateAsJavaUtilDate(java.util.Date lastLoginDate){
		Instant instant = lastLoginDate.toInstant();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		this.lastLoginDate = instant.atZone(defaultZoneId);
		
		//logger.debug("날짜: {}", new Date());
	}
	
	/**
	 * java.sql.Date to java.time.ZonedDateTime
	 * @param lastLoginDate
	 */
	public void setLastLoginDateAsJavaSqlDate(java.sql.Date lastLoginDate){		
		this.lastLoginDate = lastLoginDate.toLocalDate().atStartOfDay(
				ZoneId.of(
						TimeZone.getDefault().getID()
				)
		);
	}
	
	/**
	 * 마지막 로그인 시간을 밀리세컨트 형식으로 저장/변경합니다.
	 * @param lastLoginDate
	 */
	public void setLastLoginDateAsMillis(Long lastLoginDate){
		setLastLoginDateAsJavaUtilDate( new java.util.Date(lastLoginDate) );
	}

	@Override
	public String toString(){
		return "MemberDTO [memNo=" + memNo + ", memId=" + memId + ", memPassword=" + "[PROTECT]" + ", memNicknm="
				+ memNicknm + ", memState=" + memState + ", lastLoginDate=" + lastLoginDate + "]";
	}
}
