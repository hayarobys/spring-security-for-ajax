package com.suph.security;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("securedObjectDAO")
public interface SecuredObjectDAO{
	
	/**
	 * url, method, pointcut 형식의 자원과 권한 매핑 정보를 조회합니다.
	 * @param type DB에서 조회할 패턴의 타입을 지정합니다. url, method, pointcut이 가능합니다.
	 * @return List<RoleVO>
	 */
	public abstract List<RoleVO> getRolesAndResources(String type);
}

