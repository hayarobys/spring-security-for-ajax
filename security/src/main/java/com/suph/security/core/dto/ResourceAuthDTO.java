package com.suph.security.core.dto;

/**
 * 리소스-권한 정보를 담는데 사용됩니다.
 * (리소스 접근에 필요한 권한 정보)
 */
// TB_RESOURCE_AUTH 테이블
public class ResourceAuthDTO{
	/** 리소스 일련 번호 */
	private int resourceNo;
	/** 리소스 이름, URL 패턴 */
	private String resourceNm;	// pattern
	/** 권한 일련 번호 */
	private int authNo;
	/** 접근 가능한 권한(필요권한) */
	private String authNm;	// authority
	
	
	/**
	 * 리소스 일련 번호를 반환 합니다.
	 * @return
	 */
	public int getResourceNo(){
		return resourceNo;
	}

	/**
	 * 리소스 일련 번호를 저장/변경 합니다.
	 * @param resourceNo
	 */
	public void setResourceNo(int resourceNo){
		this.resourceNo = resourceNo;
	}

	/**
	 * URL패턴을 반환 합니다.
	 * @return
	 */
	public String getResourceNm(){
		return resourceNm;
	}

	/**
	 * URL패턴을 저장/변경 합니다.
	 * @param pattern
	 */
	public void setResourceNm(String resourceNm){
		this.resourceNm = resourceNm;
	}
	
	/**
	 * 권한 일련 번호를 반환 합니다.
	 * @return
	 */
	public int getAuthNo(){
		return authNo;
	}

	/**
	 * 권한 일련 번호를 저장/변경 합니다.
	 * @param authNo
	 */
	public void setAuthNo(int authNo){
		this.authNo = authNo;
	}

	/**
	 * 필요권한을 반환 합니다.
	 * @return
	 */
	public String getAuthNm(){
		return authNm;
	}
	
	/**
	 * 필요 권한을 저장/변경 합니다.
	 * @param authority
	 */
	public void setAuthNm(String authNm){
		this.authNm = authNm;
	}

	@Override
	public String toString(){
		return "RoleVO [resourceNm=" + resourceNm + ", authNm=" + authNm + "]";
	}
}
