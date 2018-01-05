package com.suph.security;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Repository;


public interface SecuredObjectService{
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
}
