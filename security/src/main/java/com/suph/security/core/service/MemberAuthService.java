package com.suph.security.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.suph.security.core.dto.MemberAuthDTO;

public interface MemberAuthService{
	public List<GrantedAuthority> loadUserAuthorities(Integer memNo);
	
	/**
	 * 특정 계정이 보유한 권한 목록 반환
	 * @param memNo 조회할 계정의 일련 번호
	 */
	public Map<String, Object> getAuthOfMemberByMemNo(Integer memNo);
	
	/**
	 * 툭정 계정이 보유한 권한 목록 업데이트
	 * @param memNo 수정할 계정의 일련 번호
	 */
	public Map<String, Object> patchAuthOfMemberByMemNo(Integer memNo, MemberAuthDTO memberAuthDTO);
}
