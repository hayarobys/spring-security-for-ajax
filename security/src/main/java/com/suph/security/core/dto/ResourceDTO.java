package com.suph.security.core.dto;

import java.util.List;


/**
 * 리소스 정보를 담는데 사용됩니다.
 */
// TB_RESOURCE 테이블
public class ResourceDTO{
	/** 리소스 일련 번호 */
	private Integer resourceNo;
	/** 리소스 순서 */
	private Integer sortOrder;
	/** 리소스 타입 */
	private String resourceType;
	/** HTTP METHOD */
	private String httpMethod;
	/** 리소스 패턴 */
	private String resourcePattern;
	/** 리소스 이름 */
	private String resourceNm;
	
	private List<ResourceDTO> list;
	
	public Integer getResourceNo(){
		return resourceNo;
	}
	
	public void setResourceNo(Integer resourceNo){
		this.resourceNo = resourceNo;
	}
	
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}
	
	public String getResourceType(){
		return resourceType;
	}
	
	public void setResourceType(String resourceType){
		this.resourceType = resourceType.trim();
	}
	
	public String getHttpMethod(){
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod){
		this.httpMethod = httpMethod.trim();
	}

	public String getResourcePattern(){
		return resourcePattern;
	}
	
	public void setResourcePattern(String resourcePattern){
		this.resourcePattern = resourcePattern.trim();
	}
	
	public String getResourceNm(){
		return resourceNm;
	}
	
	public void setResourceNm(String resourceNm){
		this.resourceNm = resourceNm.trim();
	}

	public List<ResourceDTO> getList(){
		return list;
	}

	public void setList(List<ResourceDTO> list){
		this.list = list;
	}

	@Override
	public String toString(){
		return "ResourceDTO [resourceNo=" + resourceNo + ", sortOrder=" + sortOrder + ", resourceType=" + resourceType
				+ ", httpMethod=" + httpMethod + ", resourcePattern=" + resourcePattern + ", resourceNm=" + resourceNm
				+ ", list=" + list + "]";
	}
}
