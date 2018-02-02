package com.suph.security.core.userdetails.jdbc.dao;

import org.springframework.stereotype.Repository;

import com.suph.security.core.userdetails.jdbc.vo.MemberVO;

@Repository
public interface MemberDAO{
	/**
	 * 계정을 등록합니다.
	 * @param vo
	 * @return
	 */
	public abstract int insertMemberVO(MemberVO vo);
}
