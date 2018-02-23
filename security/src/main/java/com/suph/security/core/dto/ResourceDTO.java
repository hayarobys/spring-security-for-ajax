package com.suph.security.core.dto;

import java.util.List;


/**
 * 리소스 정보를 담는데 사용됩니다.
 */
// TB_RESOURCE 테이블
public class ResourceDTO{
	/** 리소스 일련 번호 */
	private int resourceNo;
	/** 리소스 순서 */
	private int sortOrder;
	/** 리소스 타입 */
	private String resourceType;
	/** 리소스 패턴 */
	private String resourcePattern;
	/** 리소스 이름 */
	private String resourceNm;
	
	private List<ResourceDTO> list;
	
	public int getResourceNo(){
		return resourceNo;
	}
	
	public void setResourceNo(int resourceNo){
		this.resourceNo = resourceNo;
	}
	
	public int getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(int sortOrder){
		this.sortOrder = sortOrder;
	}
	
	public String getResourceType(){
		return resourceType;
	}
	
	public void setResourceType(String resourceType){
		this.resourceType = resourceType;
	}
	
	public String getResourcePattern(){
		return resourcePattern;
	}
	
	public void setResourcePattern(String resourcePattern){
		this.resourcePattern = resourcePattern;
	}
	
	public String getResourceNm(){
		return resourceNm;
	}
	
	public void setResourceNm(String resourceNm){
		this.resourceNm = resourceNm;
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
				+ ", resourcePattern=" + resourcePattern + ", resourceNm=" + resourceNm + ", list=" + list + "]";
	}
}
