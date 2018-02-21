package com.suph.security.core.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suph.security.core.dao.AuthDAO;
import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.service.AuthService;

@Service("authService")
public class AuthServiceImpl implements AuthService{
	@Autowired
	private AuthDAO dao;
	
	/**
	 * 모든 권한 목록을 조회합니다.
	 */
	@Override
	public Map<String, Object> getAuthList(){
		Map<String, Object> result = new HashMap<String, Object>();
		List<AuthDTO> list = null;
		
		try{
			list = dao.getAuthList();
			
			result.put("list", list);
			result.put("result", "success");
		}catch(SQLException sqle){
			sqle.printStackTrace();
			result.put("result", "fail");
		}
		
		return result;
	}
}
