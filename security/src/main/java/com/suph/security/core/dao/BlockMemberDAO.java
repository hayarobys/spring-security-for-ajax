package com.suph.security.core.dao;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.BlockMemberDTO;

@Repository
public interface BlockMemberDAO{
	/**
	 * 특정 계정이 차단 상태인지 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract BlockMemberDTO selectBlockMemberByMemNo(Integer memNo);
	
	/**
	 * 특정 계정이 차단 상태인지 조회합니다.
	 * 단, 현재 시간 이전의 차단 이력은 조회하지 않습니다.
	 * @param memNo
	 * @return
	 */
	public abstract BlockMemberDTO selectBlockMemberByMemNoAndAfterCurrentDate(Integer memNo);
}
