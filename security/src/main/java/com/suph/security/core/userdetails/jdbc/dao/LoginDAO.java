package com.suph.security.core.userdetails.jdbc.dao;

import org.springframework.stereotype.Repository;

import com.suph.security.core.userdetails.jdbc.vo.MemberVO;

@Repository("loginDAO")
public interface LoginDAO{
	/**
	 * DB로부터 해당 ID를 가진 계정의 정보를 조회하는 메서드
	 * 활성화 계정만 조회합니다. 탈퇴, 블럭 등으로 비활성화 된 계정은 대상에서 제외됩니다.
	 * @param id 조회할 계정 ID
	 * @return 계정 정보 반환
	 */
	public abstract MemberVO getMemberInfoById(String id);
	
}
