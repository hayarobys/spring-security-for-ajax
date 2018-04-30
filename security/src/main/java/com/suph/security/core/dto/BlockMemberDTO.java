package com.suph.security.core.dto;

/**
 * TB_BLOCK_MEMBER 차단 계정 테이블의 정보를 옮기는 역할
 */
public class BlockMemberDTO{
	/**
	 * 실제 존재하는 컬럼이 아닌, SQL문으로 concat함수를 이용해 생성한 가상 컬럼 입니다.
	 * ex) tester/테스터/11 아이디/닉네임/계정일련번호
	 * dataField는 blockNo로, displayField는 memInfo로 출력하는데 쓰입니다.
	 */
	private String memInfo;
	
	/** 차단 일련 번호 */
	private Integer blockNo;
	/** 계정 일련 번호 */
	private Integer memNo;
	/** 차단 시작 일자 */
	private java.util.Date blockStartDate;
	/** 차단 만료 일자 */
	private java.util.Date blockExpireDate;
	/** 차단 사유 */
	private String blockCause;
	
	public String getMemInfo(){
		return memInfo;
	}
	
	public void setMemInfo(String memInfo){
		this.memInfo = memInfo;
	}
	
	public Integer getBlockNo(){
		return blockNo;
	}

	public void setBlockNo(Integer blockNo){
		this.blockNo = blockNo;
	}
	
	public Integer getMemNo(){
		return memNo;
	}
	
	public void setMemNo(Integer memNo){
		this.memNo = memNo;
	}
	
	public java.util.Date getBlockStartDate(){
		return blockStartDate;
	}
	
	public void setBlockStartDate(java.util.Date blockStartDate){
		this.blockStartDate = blockStartDate;
	}
	
	public java.util.Date getBlockExpireDate(){
		return blockExpireDate;
	}
	
	public void setBlockExpireDate(java.util.Date blockExpireDate){
		this.blockExpireDate = blockExpireDate;
	}
	
	public String getBlockCause(){
		return blockCause;
	}
	
	public void setBlockCause(String blockCause){
		this.blockCause = blockCause;
	}

	@Override
	public String toString(){
		return "BlockMemberDTO [memInfo=" + memInfo + ", blockNo=" + blockNo + ", memNo=" + memNo + ", blockStartDate="
				+ blockStartDate + ", blockExpireDate=" + blockExpireDate + ", blockCause=" + blockCause + "]";
	}
}


