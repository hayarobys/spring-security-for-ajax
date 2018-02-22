package com.suph.security.core.service;

import java.util.Map;

import com.suph.security.core.dto.ResourceDTO;

public interface ResourceService{
	/**
	 * 리소스 목록을 조회합니다.
	 * @return
	 */
	public abstract Map<String, Object> getResourceList();
	
	/**
	 * 특정 URL접근에 필요한 권한 목록을 조회합니다.
	 * @param authNo
	 * @return
	 */
	public abstract Map<String, Object> getAuthListByResourceNo(int resourceNo);
	
	/**
	 * 리소스를 등록합니다.
	 * @param resourceDTO
	 * @return
	 */
	public abstract Map<String, Object> insertResource(ResourceDTO resourceDTO);
}
