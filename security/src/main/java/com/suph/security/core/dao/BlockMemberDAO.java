package com.suph.security.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.BlockMemberDTO;

@Repository
public interface BlockMemberDAO{
	/**
	 * 특정 계정의 차단 이력을 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract List<BlockMemberDTO> selectBlockMemberByMemNo(Integer memNo);
	
	/**
	 * 특정 계정이 현재 차단 상태인지 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract BlockMemberDTO selectBlockMemberByMemNoAndAfterCurrentDate(Integer memNo);
	
	/**
	 * 모든 차단 계정 정보를 조회합니다.
	 * @return
	 */
	public abstract List<BlockMemberDTO> selectBlockMember();
	
	/**
	 * 차단 계정을 추가합니다.
	 * @param blockMemberDTO
	 */
	public abstract void insertBlockMember(BlockMemberDTO blockMemberDTO);
	
	/**
	 * 차단 계정 정보를 수정합니다.
	 * @param blockMemberDTO
	 */
	public abstract void updateBlockMember(BlockMemberDTO blockMemberDTO);
	
	/**
	 * 차단 계정 정보를 삭제 합니다.
	 * @param memNo
	 */
	public abstract void deleteBlockMember(Integer memNo);
}
