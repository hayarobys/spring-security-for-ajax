package com.suph.security;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberDAO{
	/**
	 * 계정을 등록합니다.
	 * @param vo
	 * @return
	 */
	public abstract int insertMemberVO(MemberVO vo);
}
