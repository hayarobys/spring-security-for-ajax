package com.suph.security.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suph.security.core.service.RealTimeReflectableService;
import com.suph.security.web.access.intercept.ReloadableFilterInvocationSecurityMetadataSource;

@Service
public class RealTimeReflectableImpl implements RealTimeReflectableService{
	@Autowired
	ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource;
	
	@Override
	public Map<String, Object> reload(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			reloadableFilterInvocationSecurityMetadataSource.reload();
			returnMap.put("result", "success");
		}catch(Exception e){
			returnMap.put("result", "fail");
			e.printStackTrace();
		}
		
		return returnMap;
	}

}
