package com.suph.security.core.dto;

/** 계정 보유 권한을 옮기는데 사용합니다 */
//TB_AUTH 권한 테이블
public class AuthDTO{
	/** AUTH_NO 권한 일련 번호 */
	private Integer authNo;		
	/** AUTH_NM 권한 명 */
	private String authNm;
	/** AUTH_EXPLANATION 권한 설명 */
	private String authExplanation;
	
	/**
	 * 권한 일련 번호를 반환합니다.
	 * @return
	 */
	public Integer getAuthNo(){
		return authNo;
	}
	
	/**
	 * 이 권한의 일련 번호를 저장/변경 합니다.
	 * @param authNo
	 */
	public void setAuthNo(Integer authNo){
		this.authNo = authNo;
	}
	
	/**
	 * 권한명을 반환합니다.
	 * @return
	 */
	public String getAuthNm(){
		return authNm;
	}
	
	/**
	 * 권한명을 저장/변경 합니다.
	 * @param authNm
	 */
	public void setAuthNm(String authNm){
		this.authNm = authNm.trim();
	}
	
	/**
	 * 권한 설명을 반환합니다.
	 * @return
	 */
	public String getAuthExplanation(){
		return authExplanation;
	}
	
	/**
	 * 권한 설명을 저장/변경 합니다.
	 * @param authExplanation
	 */
	public void setAuthExplanation(String authExplanation){
		this.authExplanation = authExplanation.trim();
	}

	@Override
	public String toString(){
		return "AuthDTO [authNo=" + authNo + ", authNm=" + authNm + ", authExplanation=" + authExplanation + "]";
	}
}
