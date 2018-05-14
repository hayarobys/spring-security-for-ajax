package com.suph.security.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.BlockInfoDTO;
import com.suph.security.core.dto.BlockMemberDTO;
import com.suph.security.core.dto.SearchBlockMemberDTO;

@Repository
public interface BlockMemberDAO{
	/**
	 * 특정 계정의 과거 포함 모든 차단 이력을 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract List<BlockMemberDTO> selectBlockMemberByMemNo(Integer memNo);
	
	/**
	 * 현시간 기준, 특정 계정의 차단 종료일이 남아 있을 경우, 그 차단 정보를 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract List<BlockInfoDTO> selectBlockMemberByMemNoAndExpireDateIsAfterTheCurrentDate(Integer memNo);
	
	/**
	 * 특정 계정의 현재와 미래의 차단 목록을 조회합니다.
	 * @return
	 */
	public abstract List<BlockInfoDTO> selectBlockMemberExpireDateIsAfterTheCurrentDate(Integer memNo);
	
	public abstract List<BlockInfoDTO> selectCurrentBlockMemberInfoByMemNo(Integer memNo);
	
	/**
	 * 현시간 기준, 검색조건에 따른 과거/현재/미래의 차단 목록을 반환합니다.
	 * @param searchBlockMemberDTO
	 * @return
	 */
	public abstract List<BlockMemberDTO> selectBlockMemberBySearchValue(SearchBlockMemberDTO searchBlockMemberDTO);
	
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
	 * @param blockNo
	 */
	public abstract void deleteBlockMember(Integer blockNo);
}
