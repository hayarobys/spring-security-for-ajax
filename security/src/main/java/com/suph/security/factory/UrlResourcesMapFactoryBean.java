package com.suph.security.factory;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.suph.security.core.service.ResourceAuthService;

public class UrlResourcesMapFactoryBean
	implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>>{
	
	@Autowired
	//@Qualifier("securedObjectServiceImpl")
	private ResourceAuthService resourceAuthService;
	private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;
	
	public void setResourceAuthService(ResourceAuthService resourceAuthService){
		this.resourceAuthService = resourceAuthService;
	}
	
	public void init() throws Exception{
		requestMap = resourceAuthService.getRolesAndUrl();
	}
	
	/*
	 * 싱글턴
	 */
	@Override
	public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception{
		if(requestMap == null){
			requestMap = resourceAuthService.getRolesAndUrl();
		}
		return requestMap;
	}
	
	@Override
	public Class<?> getObjectType(){
		return LinkedHashMap.class;
	}
	
	@Override
	public boolean isSingleton(){
		return true;
	}
}
