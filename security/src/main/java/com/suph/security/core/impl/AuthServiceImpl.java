package com.suph.security.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.suph.security.core.dao.AuthDAO;
import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.PaginationRequest;
import com.suph.security.core.dto.PaginationResponse;
import com.suph.security.core.service.AuthService;

@Service("authService")
public class AuthServiceImpl implements AuthService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AuthDAO authDAO;
	
	@Override
	public Map<String, Object> getAuthList(PaginationRequest paginationRequest){
		Map<String, Object> result = new HashMap<String, Object>();
		
		try{
			List<AuthDTO> list = authDAO.getAuthList(paginationRequest);
			int totalRows = authDAO.getAuthListTotalRows();
			
			PaginationResponse<AuthDTO> data = new PaginationResponse<AuthDTO>(list, totalRows);
			
			result.put("list", data);
			result.put("result", "success");
		}catch(DataAccessException sqle){
			sqle.printStackTrace();
			result.put("result", "fail");
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> postAuth(AuthDTO authDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		StringBuffer errorInfo = new StringBuffer();
		boolean isValid = true;
		if( !StringUtils.hasText(authDTO.getAuthNm()) ){
			errorInfo.append("\n권한 명이 없습니다.");
			isValid = false;
		}
		logger.debug("권한명이 있는가? : {}", StringUtils.hasText(authDTO.getAuthNm()));
		if( !StringUtils.hasText(authDTO.getAuthExplanation()) ){
			errorInfo.append("\n권한 설명이 없습니다.");
			isValid = false;
		}
		
		if(isValid == true){
			try{
				authDAO.insertAuth(authDTO);
				returnMap.put("result", "success");
			}catch(DataAccessException sqle){
				sqle.printStackTrace();
				returnMap.put("result", "fail");
			}
		}else{
			returnMap.put("result", "fail");
		}
		
		returnMap.put("message", errorInfo.toString());
		return returnMap;
	}
	
	@Override
	public Map<String, Object> patchAuthByAuthNo(Integer authNo, AuthDTO authDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		authDTO.setAuthNo(authNo);
		try{
			authDAO.updateAuthByAuthNo(authDTO);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}
	
	@Override
	public Map<String, Object> deleteAuthByAuthNo(Integer authNo){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if(authNo == null){
			returnMap.put("result", "fail");
			returnMap.put("message", "삭제할 권한 일련 번호로 null값이 올 수 없습니다.");
		}else{
			StringBuffer errorInfo = new StringBuffer();
			
			try{
				authDAO.deleteAuthByAuthNo(authNo);
				returnMap.put("result", "success");
			}catch(DataIntegrityViolationException keyException){
				errorInfo.append("\nauthNo: ");
				errorInfo.append(authNo);
				errorInfo.append("\ncause: 제약조건에 위배되어 제거할 수 없습니다.");
				
				returnMap.put("message", errorInfo.toString());
				
				logger.debug("다음의 권한 제거중 오류가 발생했습니다.\n{}",errorInfo.toString());
				errorInfo.delete(0, errorInfo.length());
				
				keyException.printStackTrace();
			}catch(DataAccessException e){
				// 제거 실패한 권한의 정보를 따로 보관한다.
				errorInfo.append("\nauthNo: ");
				errorInfo.append(authNo);
				errorInfo.append("\ncause: 알 수 없는 이유로 제거에 실패했습니다.");
				
				returnMap.put("message", errorInfo.toString());
				
				logger.debug("다음의 권한 제거중 오류가 발생했습니다.\n{}",errorInfo.toString());
				errorInfo.delete(0, errorInfo.length());
				
				e.printStackTrace();
			}
		}
		
		return returnMap;
	}
}
