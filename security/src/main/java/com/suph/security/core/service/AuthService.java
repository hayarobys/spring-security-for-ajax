package com.suph.security.core.service;

import java.util.Map;

import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.PaginationRequest;

public interface AuthService{
	/**
	 * 모든 권한 목록을 조회합니다.
	 * @param paginationRequest 페이징 검색 조건
	 * @return
	 */
	public abstract Map<String, Object> getAuthList(PaginationRequest paginationRequest);
	
	/**
	 * 권한을 등록합니다.
	 * @param authDTO
	 * @return
	 */
	public abstract Map<String, Object> postAuth(AuthDTO authDTO);
	
	/**
	 * 특정 권한을 수정합니다.
	 * @param authNo
	 * @param authDTO
	 * @return
	 */
	public abstract Map<String, Object> patchAuthByAuthNo(Integer authNo, AuthDTO authDTO);
	
	/**
	 * 특정 권한을 삭제합니다.
	 * @return
	 */
	public abstract Map<String, Object> deleteAuthByAuthNo(Integer authNo);
}
