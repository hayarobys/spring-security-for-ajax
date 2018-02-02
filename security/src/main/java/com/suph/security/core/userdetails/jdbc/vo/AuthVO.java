package com.suph.security.core.userdetails.jdbc.vo;

/** 계정 보유 권한을 옮기는데 사용합니다 */
// TB_AUTH 권한 테이블
public class AuthVO{
	/** AUTH_NO 권한 일련 번호 */
	private int no;		
	/** AUTH_NM 권한 명 */
	private String name;
	/** AUTH_EXPLANATION 권한 설명 */
	private String explanation;
	
	/**
	 * 권한 일련 번호를 반환합니다.
	 * @return
	 */
	public int getNo(){
		return no;
	}
	
	/**
	 * 이 권한의 일련 번호를 저장/변경 합니다.
	 * @param no
	 */
	public void setNo(int no){
		this.no = no;
	}
	
	/**
	 * 권한명을 반환합니다.
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 권한명을 저장/변경 합니다.
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 권한 설명을 반환합니다.
	 * @return
	 */
	public String getExplanation(){
		return explanation;
	}
	
	/**
	 * 권한 설명을 저장/변경 합니다.
	 * @param explanation
	 */
	public void setExplanation(String explanation){
		this.explanation = explanation;
	}
}
