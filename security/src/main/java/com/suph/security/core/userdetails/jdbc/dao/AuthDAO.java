package com.suph.security.core.userdetails.jdbc.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.userdetails.jdbc.vo.AuthVO;

@Repository("authDAO")
public interface AuthDAO{
	/**
	 * 해당 유저 일련 번호가 소유한 권한 목록을 반환합니다.
	 * @param memNo
	 * @return
	 */
	public abstract List<AuthVO> getAuthListByNo(int memNo) throws SQLException;
	
	/**
	 * 모든 권한 목록을 반환합니다.
	 * @return
	 */
	public abstract List<AuthVO> getAuthList() throws SQLException;
}
