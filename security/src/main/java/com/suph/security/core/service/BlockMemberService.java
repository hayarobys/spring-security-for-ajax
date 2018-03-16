package com.suph.security.core.service;

import java.util.Map;

import com.suph.security.core.dto.BlockMemberDTO;

public interface BlockMemberService{
	/**
	 * 특정 계정의 차단 이력을 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract Map<String, Object> getBlockMemberByMemNo(Integer memNo);
	
	/**
	 * 모든 차단 계정 목록을 반환 합니다.
	 * @return
	 */
	public abstract Map<String, Object> getBlockMember();
	
	/**
	 * 차단 계정을 추가 합니다.
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	public abstract Map<String, Object> postBlockMember(BlockMemberDTO blockMemberDTO);
	
	/**
	 * 차단 계정 정보를 수정 합니다.
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	public abstract Map<String, Object> patchBlockMember(Integer memNo, BlockMemberDTO blockMemberDTO);
	
	/**
	 * 차단 계정 정보를 삭제 합니다.
	 * @param memNo
	 * @return
	 */
	public abstract Map<String, Object> deleteBlockMember(Integer memNo);
}
