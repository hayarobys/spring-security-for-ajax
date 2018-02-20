package com.suph.security.core.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.ResourceAuthDTO;

@Repository
public interface ResourceAuthDAO{
	/**
	 * url, method, pointcut 형식의 자원과 권한 매핑 정보를 조회합니다.
	 * @param type DB에서 조회할 패턴의 타입을 지정합니다. url, method, pointcut이 가능합니다.
	 * @return List<RoleVO>
	 */
	public abstract List<ResourceAuthDTO> getRolesAndResources(String type);
	
	/**
	 * 특정 URL 접근에 필요한 권한 목록을 조회합니다.
	 * @param 권한 조회할 패턴
	 * @return
	 */
	public abstract List<AuthDTO> getAuthListByResourceNo(Map<String, Object> search);
}
