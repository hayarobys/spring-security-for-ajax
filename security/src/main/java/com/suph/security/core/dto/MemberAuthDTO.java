package com.suph.security.core.dto;

/**
 * 계정-권한 정보를 담는데 사용합니다.
 */
public class MemberAuthDTO{
	/** MEM_NO: 계정 일련 번호 */
	private String no;
	/** AUTH_NM: 부여할 권한 */
	private String auth;
	
	public String getNo(){
		return no;
	}
	
	public void setNo(String no){
		this.no = no;
	}
	
	public String getAuth(){
		return auth;
	}
	
	public void setAuth(String auth){
		this.auth = auth;
	}
}
