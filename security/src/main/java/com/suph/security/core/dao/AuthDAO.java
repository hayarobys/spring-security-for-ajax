package com.suph.security.core.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.AuthDTO;

@Repository
public interface AuthDAO{
	
	/**
	 * 모든 권한 목록을 반환합니다.
	 * @return
	 */
	public abstract List<AuthDTO> getAuthList() throws SQLException;
}
