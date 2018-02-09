package com.suph.security.core.resourcedetails.jdbc.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.resourcedetails.jdbc.vo.ResourceVO;

@Repository("resourceDAO")
public interface ResourceDAO{
	
	/**
	 * 리소스 목록을 조회합니다.
	 * @return
	 */
	public abstract List<ResourceVO> getResourceList() throws SQLException;
}