package com.suph.security.core.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.suph.security.core.dto.ResourceAuthDTO;

public interface ResourceAuthService{
	/**
	 * URL 형식의 자원과 권한의 매핑 정보를 조회합니다.
	 * @return
	 * @throws Exception
	 */
	public abstract LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() throws Exception;
	
	/**
	 * METHOD 형식의 자원과 권한의 매핑 정보를 조회합니다.
	 * @return
	 * @throws Exception
	 */
	public abstract LinkedHashMap<String, List<ConfigAttribute>> getRolesAndMethod() throws Exception;
	
	/**
	 * AOP POINTCUT 형식의 자원과 권한의 매핑 정보를 조회합니다.
	 * @return
	 * @throws Exception
	 */
	public abstract LinkedHashMap<String, List<ConfigAttribute>> getRolesAndPointcut() throws Exception;
	
	/**
	 * 특정 RESOURCE 접근에 필요한 AUTH 목록을 변경합니다.
	 * @param resourceAuthDTO
	 * @return
	 */
	public abstract Map<String, Object> changeResourceAuth(ResourceAuthDTO resourceAuthDTO);
	
}
