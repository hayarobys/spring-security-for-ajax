package com.suph.security.core.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.suph.security.core.dao.ResourceAuthDAO;
import com.suph.security.core.dao.ResourceDAO;
import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.ResourceDTO;
import com.suph.security.core.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService{
	@Autowired
	private ResourceDAO resourceDAO;
	
	@Autowired
	private ResourceAuthDAO securedObjectDAO;
	
	@Override
	public Map<String, Object> getResourceList(){
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ResourceDTO> list = null;
		try{
			 list = resourceDAO.getResourceList();
			 result.put("result", "success");
			 result.put("list", list);
			 
		}catch(SQLException sqle){
			sqle.printStackTrace();
			result.put("result", "fail");
			
		}
				
		return result;
	}

	@Override
	public Map<String, Object> getAuthListByResourceNo(int resourceNo){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> search = new HashMap<String, Object>();
		
		search.put("type", "url");
		search.put("resourceNo", resourceNo);
		
		List<AuthDTO> list = securedObjectDAO.getAuthListByResourceNo(search);
		
		result.put("result", "success");
		result.put("list", list);
		
		return result;
	}

	@Override
	public Map<String, Object> insertResource(ResourceDTO resourceDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String result = "";
		try{ 
			resourceDAO.insertResource(resourceDTO);
			result = "success";
		}catch(DataAccessException e){
			result = "fail";
		}
		returnMap.put("result", result);
		return returnMap;
	}

}
