package com.suph.security.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.PaginationRequest;
import com.suph.security.core.dto.ResourceDTO;

@Repository
public interface ResourceDAO{
	/**
	 * 리소스의 전체 항목 개수를 조회합니다.
	 * @return
	 */
	public abstract int getResourceListTotalRows();
	
	/**
	 * 리소스 목록을 조회합니다.
	 * @param paginationRequest 페이지 정보
	 * @return
	 */
	public abstract List<ResourceDTO> getResourceList(PaginationRequest paginationRequest);
	
	/**
	 * 리소스를 등록합니다.
	 * @param resourceDTO 등록할 리소스 정보. resourceNo는 포함 여부에 관계없이 자동 생성됩니다.
	 */
	public abstract void insertResource(ResourceDTO resourceDTO);

	/**
	 * 리소스를 수정합니다.
	 * @param resourceDTO 수정할 리소스 정보. resourceNo는 필수 값 입니다.
	 */
	public abstract void updateResourceByResourceNo(ResourceDTO resourceDTO);
	
	/**
	 * 특정 리소스를 제거합니다.
	 * @param resourceNo 제거할 리소스의 일련 번호를 입력합니다.
	 */
	public abstract void deleteResource(int resourceNo);
}
