package com.suph.security.core.dto;

/**
 * TB_MEMBER_STATE(계정 상태 테이블)의 데이터를 처리하는데 사용합니다.
 * @author NB-0267
 *
 */
public class MemberStateDTO{	
	/** 상태 일련 번호 */
	private int stateNo;
	/** 상태 명 */
	private String stateNm;
	/** 상태 설명 */
	private String stateExplanation;
	
	public int getStateNo(){
		return stateNo;
	}

	public void setStateNo(int stateNo){
		this.stateNo = stateNo;
	}

	public String getStateNm(){
		return stateNm;
	}

	public void setStateNm(String stateNm){
		this.stateNm = stateNm.trim();
	}

	public String getStateExplanation(){
		return stateExplanation;
	}

	public void setStateExplanation(String stateExplanation){
		this.stateExplanation = stateExplanation.trim();
	}

	@Override
	public String toString(){
		return "MemberStateDTO [stateNo=" + stateNo + ", stateNm=" + stateNm + ", stateExplanation=" + stateExplanation
				+ "]";
	}
}
