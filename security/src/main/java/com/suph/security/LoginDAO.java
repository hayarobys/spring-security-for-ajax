package com.suph.security;

import org.springframework.stereotype.Repository;

@Repository("loginDAO")
public interface LoginDAO{
	/**
	 * DB로부터 해당 ID를 가진 계정의 정보를 조회하는 메서드
	 * @param id 조회할 계정 ID
	 * @return 계정 정보 반환
	 */
	public abstract MemberInfo getMemberInfoById(String id);
	
}
