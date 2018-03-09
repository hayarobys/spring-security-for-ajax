package com.suph.security.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.MemberDTO;


@Repository
public interface MemberDAO{
	/**
	 * 계정을 등록합니다.
	 * @param vo
	 * @return
	 */
	public abstract int insertMemberVO(MemberDTO vo);
	
	/**
	 * DB로부터 해당 ID를 가진 계정의 정보를 조회하는 메서드
	 * 활성화 계정만 조회합니다. 탈퇴, 블럭 등으로 비활성화 된 계정은 대상에서 제외됩니다.
	 * @param id 조회할 계정 ID
	 * @return 계정 정보 반환
	 */
	public abstract MemberDTO getMemberInfoById(String id);
	
	/**
	 * DB로부터 모든 활성 계정 정보를 조회합니다.
	 * 단, 패스워드는 조회하지 않습니다.
	 * @return
	 */
	public abstract List<MemberDTO> selectMember();
	
	/**
	 * DB에 입력 ID와 일치하는 값이 있는지 조회합니다.
	 * ID중복검사에 사용합니다.
	 * @param memId 조회할 ID
	 * @return memId 동일한 ID가 있다면 해당 ID를 그대로 반환합니다. 없다면 null값이 반환 됩니다.
	 */
	public abstract String selectMemId(String memId);
}
