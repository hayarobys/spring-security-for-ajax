package com.suph.security.core.dto;

import java.util.List;

/**
 * 리소스-권한 정보를 담는데 사용됩니다.
 * (리소스 접근에 필요한 권한 정보)
 */
// TB_RESOURCE_AUTH 테이블
public class ResourceAuthDTO{
	/** 리소스 일련 번호 */
	private Integer resourceNo;
	/** HTTP Method 패턴 (GET / POST / PATCH / DELETE / ...) */
	private String httpMethod;
	/** 리소스 이름, URL 패턴 */
	private String resourceNm;	// pattern
	/** 권한 일련 번호 */
	private Integer authNo;
	/** 접근 가능한 권한(필요권한) */
	private String authNm;	// authority
	/** 접근 가능한 권한 목록 */
	private List<Integer> authNoList;
	
	/**
	 * 리소스 일련 번호를 반환 합니다.
	 * @return
	 */
	public Integer getResourceNo(){
		return resourceNo;
	}

	/**
	 * 리소스 일련 번호를 저장/변경 합니다.
	 * @param resourceNo
	 */
	public void setResourceNo(Integer resourceNo){
		this.resourceNo = resourceNo;
	}
	
	/**
	 * HTTP Method를 반환 합니다. (GET / PUT / PATCH / DELETE / ...)
	 * @return httpMethod
	 */
	public String getHttpMethod(){
		return httpMethod;
	}

	/**
	 * HTTP Method를 저장/변경 합니다. (GET / PUT / PATCH / DELETE / ...)
	 * @param httpMethod
	 */
	public void setHttpMethod(String httpMethod){
		this.httpMethod = httpMethod.trim();
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
		this.resourceNm = resourceNm.trim();
	}
	
	/**
	 * 권한 일련 번호를 반환 합니다.
	 * @return
	 */
	public Integer getAuthNo(){
		return authNo;
	}

	/**
	 * 권한 일련 번호를 저장/변경 합니다.
	 * @param authNo
	 */
	public void setAuthNo(Integer authNo){
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
		this.authNm = authNm.trim();
	}

	/**
	 * 권한 일련 번호 목록을 조회 합니다.
	 * @return
	 */
	public List<Integer> getAuthNoList(){
		return authNoList;
	}

	/**
	 * 권한 일련 번호 목록을 저장/변경 합니다.
	 * @param authNoList
	 */
	public void setAuthNoList(List<Integer> authNoList){
		this.authNoList = authNoList;
	}

	@Override
	public String toString(){
		return "ResourceAuthDTO [resourceNo=" + resourceNo + ", httpMethod=" + httpMethod + ", resourceNm=" + resourceNm
				+ ", authNo=" + authNo + ", authNm=" + authNm + ", authNoList=" + authNoList + "]";
	}
}
