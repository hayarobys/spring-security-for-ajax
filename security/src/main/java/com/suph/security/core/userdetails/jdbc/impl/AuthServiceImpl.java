package com.suph.security.core.userdetails.jdbc.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suph.security.core.userdetails.AuthService;
import com.suph.security.core.userdetails.jdbc.dao.AuthDAO;
import com.suph.security.core.userdetails.jdbc.vo.AuthVO;

@Service
public class AuthServiceImpl implements AuthService{
	@Autowired
	private AuthDAO dao;
	
	/**
	 * 모든 권한 목록을 조회합니다.
	 */
	@Override
	public Map<String, Object> getAuthList(){
		Map<String, Object> result = new HashMap<String, Object>();
		List<AuthVO> list = null;
		
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
