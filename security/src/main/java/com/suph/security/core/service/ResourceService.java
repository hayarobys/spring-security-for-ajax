package com.suph.security.core.service;

import java.util.List;
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
	public abstract Map<String, Object> postResource(ResourceDTO resourceDTO);
	
	/**
	 * 리소스를 수정합니다.
	 * @param resourceDTO 변경할 resourceNo값이 필수로 담겨 있어야 합니다.
	 * @return
	 */
	public abstract Map<String, Object> patchResourceByResourceNo(ResourceDTO resourceDTO);
	
	/**
	 * 하나 혹은 여러개의 리소스를 제거합니다.
	 * @param resourceNoList 제거할 리소스의 RESOURCE_NO을 목록으로 넣습니다.
	 * @return 제거에 성공시 {"result" : "success"}를 제거에 하나라도 실패시 관련 정보를 {"result" : errorInfoList} 형식으로 반환합니다.
	 */
	public abstract Map<String, Object> deleteResourceNoList(List<Integer> resourceNoList);
}
