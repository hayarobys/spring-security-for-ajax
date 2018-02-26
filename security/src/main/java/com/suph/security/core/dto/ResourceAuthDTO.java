package com.suph.security.core.dto;

import java.util.List;

import org.springframework.http.HttpMethod;

/**
 * 리소스-권한 정보를 담는데 사용됩니다.
 * (리소스 접근에 필요한 권한 정보)
 */
// TB_RESOURCE_AUTH 테이블
public class ResourceAuthDTO{
	/** 리소스 일련 번호 */
	private int resourceNo;
	/** HTTP Method 패턴 (GET / POST / PUT / PATCH / DELETE / ...) */
	private String httpMethodPattern;
	/** 리소스 이름, URL 패턴 */
	private String resourceNm;	// pattern
	/** 권한 일련 번호 */
	private int authNo;
	/** 접근 가능한 권한(필요권한) */
	private String authNm;	// authority
	/** 접근 가능한 권한 목록 */
	private List<Integer> authNoList;
	
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
	 * HTTP Method를 반환 합니다. (GET / POST / PUT / PATCH / DELETE / ...)
	 * @return httpMethodPattern
	 */
	public String getHttpMethodPattern(){
		return httpMethodPattern;
	}

	/**
	 * HTTP Method를 저장/변경 합니다. (GET / POST / PUT / PATCH / DELETE / ...)
	 * @param httpMethodPattern
	 */
	public void setHttpMethodPattern(String httpMethodPattern){
		this.httpMethodPattern = httpMethodPattern;
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
		return "ResourceAuthDTO [resourceNo=" + resourceNo + ", httpMethodPattern=" + httpMethodPattern
				+ ", resourceNm=" + resourceNm + ", authNo=" + authNo + ", authNm=" + authNm + ", authNoList="
				+ authNoList + "]";
	}
}
