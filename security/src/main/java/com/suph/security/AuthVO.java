package com.suph.security;

// TB_AUTH 권한 테이블
public class AuthVO{
	private int no;				// AUTH_NO 권한 일련 번호
	private String name;		// AUTH_NM 권한 명
	private String explanation;	// AUTH_EXPLANATION 권한 설명
	
	public int getNo(){
		return no;
	}
	
	public void setNo(int no){
		this.no = no;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getExplanation(){
		return explanation;
	}
	
	public void setExplanation(String explanation){
		this.explanation = explanation;
	}
}
