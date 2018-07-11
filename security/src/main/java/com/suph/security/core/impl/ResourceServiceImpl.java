package com.suph.security.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.suph.security.core.dao.ResourceAuthDAO;
import com.suph.security.core.dao.ResourceDAO;
import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.PaginationRequest;
import com.suph.security.core.dto.PaginationResponse;
import com.suph.security.core.dto.ResourceDTO;
import com.suph.security.core.enums.HttpMethod;
import com.suph.security.core.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ResourceDAO resourceDAO;
	
	@Autowired
	private ResourceAuthDAO securedObjectDAO;
	
	@Override
	public Map<String, Object> getResourceList(PaginationRequest paginationRequest){
		Map<String, Object> result = new HashMap<String, Object>();
	
		try{
			List<ResourceDTO> list = resourceDAO.getResourceList(paginationRequest);
			int totalRows = resourceDAO.getResourceListTotalRows();
			
			PaginationResponse<ResourceDTO> data = new PaginationResponse<ResourceDTO>(list, totalRows);
			
			result.put("result", "success");
			result.put("list", data);
		}catch(DataAccessException dae){
			result.put("result", "fail");
			dae.printStackTrace();			
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
	public Map<String, Object> postResource(ResourceDTO resourceDTO){
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

	@Override
	public Map<String, Object> deleteResourceNoList(List<Integer> resourceNoList){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		List<String> failList = new ArrayList<String>();
		StringBuffer errorInfo = new StringBuffer();
		
		// 루프없이 한 번에 제거하는 것이 효율은 좋겠으나, 걔중에 하나라도 실패할 경우를 대비해 여러번 요청 하는 방식을 사용.
		for(Integer i : resourceNoList){
			try{
				resourceDAO.deleteResource(i.intValue());
			}catch(DataIntegrityViolationException keyException){
				errorInfo.append("\nresourceNo: ");
				errorInfo.append(i);
				errorInfo.append("\ncause: 제약조건에 위배되어 제거할 수 없습니다.");
				
				failList.add(errorInfo.toString());
				
				logger.debug("다음의 리소스 제거중 오류가 발생했습니다.\n{}",errorInfo.toString());
				errorInfo.delete(0, errorInfo.length());
				
				keyException.printStackTrace();
			}catch(DataAccessException e){
				// 제거 실패한 리소스의 정보를 따로 보관한다.
				errorInfo.append("\nresourceNo: ");
				errorInfo.append(i);
				errorInfo.append("\ncause: 알 수 없는 이유로 제거에 실패했습니다.");
				//errorInfo.append("\nmessage: ");	// 쿼리 정보가 노출될 수 있어 주석처리
				//errorInfo.append(e.getMessage());
				//errorInfo.append("\ncause: ");
				//errorInfo.append(e.getCause());
				//errorInfo.append("\nstackTrace: ");
				//errorInfo.append(e.getStackTrace());
				
				failList.add(errorInfo.toString());
				
				logger.debug("다음의 리소스 제거중 오류가 발생했습니다.\n{}",errorInfo.toString());
				errorInfo.delete(0, errorInfo.length());
				
				e.printStackTrace();
			}
		}
		
		String result = (failList.size() > 0) ? failList.toString() : "success";
		returnMap.put("result", result);
		return returnMap;
	}

	@Override
	public Map<String, Object> patchResourceByResourceNo(int resourceNo, ResourceDTO resourceDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		resourceDTO.setResourceNo(resourceNo);
		
		String result = "";
		try{
			resourceDAO.updateResourceByResourceNo(resourceDTO);
			result = "success";
		}catch(DataAccessException e){
			result = "fail";
			e.printStackTrace();
		}
		
		returnMap.put("result", result);
		
		return returnMap;
	}

	@Override
	public Map<String, Object> getHttpMethodList(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String result = "";
		
		try{
			List<HttpMethod> list = Arrays.asList(HttpMethod.values());
			returnMap.put("list", list);
			result = "success";
		}catch(Exception e){
			result = "fail";
			e.printStackTrace();
		}
		
		returnMap.put("result", result);
		return returnMap;
	}
}
