package com.suph.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ReloadableFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// UrlResourcesMapFactoryBean의 getObject()로 받아온다.
	private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;
	
	//@Autowired
	//@Qualifier("securedObjectServiceImpl")
	private SecuredObjectService securedObjectService;
	
	public void setSecuredObjectService(SecuredObjectService securedObjectService){
		this.securedObjectService = securedObjectService;
	}

	// 매개변수로 FactoryBean 타입의 객체를 넣어주면 해당 객체의 getObject()가 자동호출된다.
	// 이 경우 UrlResourcesMapFactoryBean의 getObject()가 호출되어 LinkedHashMap<RequestMatcher, List<ConfigAttribute>> 타입이 반환되었다.
	public ReloadableFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap){
		this.requestMap = requestMap;
	}
	
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException{
		HttpServletRequest request = ((FilterInvocation)object).getRequest();	// /notmember/board.do
		Collection<ConfigAttribute> result = null;
		
		// /notmember/board.do
		// /notmember/**
		for(Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()){
			if(entry.getKey().matches(request)){
				result = entry.getValue();
				break;	// <- break문이 있기에 권한 순서에 주의해야한다.
			}
		}
		
		return result;
	}
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes(){
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
		
		for(Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()){
			allAttributes.addAll(entry.getValue());
		}
		
		return allAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz){
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
	/**
	 * 권한 정보를 갱신 합니다.
	 * 
	 * @throws Exception
	 */
	public void reload() throws Exception{
		LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = securedObjectService.getRolesAndUrl();
		Iterator<Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadedMap.entrySet().iterator();
		
		// 이전 데이터 삭제
		requestMap.clear();
		
		while(iterator.hasNext()){
			Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
			requestMap.put(entry.getKey(), entry.getValue());
		}
		
		if(logger.isInfoEnabled()){
			logger.info("Secured Url Resources - Role Mappings reloaded at Runtime!");
		}
	}
}
