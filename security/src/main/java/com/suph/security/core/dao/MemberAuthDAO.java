package com.suph.security.core.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.AuthDTO;

@Repository
public interface MemberAuthDAO{
	/**
	 * 해당 유저 일련 번호가 소유한 권한 목록을 반환합니다.
	 * @param memNo
	 * @return
	 */
	public abstract List<AuthDTO> getAuthListByNo(int memNo) throws SQLException;
	
}
