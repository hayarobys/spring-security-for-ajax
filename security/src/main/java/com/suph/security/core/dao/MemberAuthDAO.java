package com.suph.security.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.MemberAuthDTO;

@Repository
public interface MemberAuthDAO{
	/**
	 * 해당 유저 일련 번호가 소유한 권한 목록을 반환합니다.
	 * @param memNo
	 * @return
	 */
	public abstract List<AuthDTO> selectAuthOfMemberByMemNo(Integer memNo);
	
	/**
	 * 특정 유저에게 권한들을 부여합니다.
	 * @param memberAuthDTO
	 */
	public abstract void insertAuthOfMemberByMemNo(MemberAuthDTO memberAuthDTO);
	
	/**
	 * 특정 유저가 보유한 권한들을 모두 제거합니다.
	 * @param memNo
	 */
	public abstract void deleteAuthOfMemberByMemNo(Integer memNo);
}
