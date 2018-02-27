package com.suph.security.core.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberDTO{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** MEM_NO 계정 일련 번호 PK */
	private Integer no;
	/** MEM_ID 계정 아이디 */
	private String id;
	/** MEM_PASSWORD 계정 비밀번호 */
	private transient String password;
	/** MEM_NICKNM 계정 사용자의 이름 또는 별명 */
	private String name;
	/** MEM_ENABLE 계정 사용 여부(탈퇴 여부). Y는 사용 중, N은 탈퇴 혹은 휴면 계정. */
	private char enable; 
	
	/**
	 * 계정 일련 번호를 반환합니다.
	 * @return no 반환
	 */
	public Integer getNo(){
		return no;
	}
	
	/**
	 * 계정 일련 번호를 변경/저장 합니다.
	 * @param no
	 */
	public void setNo(Integer no){
		this.no = no;
	}
	
	/**
	 * 계정 ID를 반환합니다.
	 * @return id 반환
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * 계정 ID를 변경/저장 합니다.
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/**
	 * 계정의 비밀번호를 반환합니다.
	 * @return password 반환
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * 계정 비밀번호를 변경/저장 합니다.
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * 계정 사용자의 이름을 반환합니다.
	 * @return name 반환
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 계정 사용자의 이름을 변경/저장 합니다.
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 계정 사용 여부를 반환 합니다.
	 * @return enable 반환
	 */
	public char getEnable(){
		return enable;
	}
	
	/**
	 * 계정 사용 여부를 변경/저장 합니다.
	 * @param enable 사용시 Y, 탈퇴 혹은 휴면 계정일 경우 N
	 */
	public void setEnable(char enable){
		this.enable = enable;
	}
	
	@Override
	public String toString(){
		return "MemberVO [no=" + no + ", id=" + id + ", name=" + name + ", enable=" + enable + "]";
	}
}
