package com.suph.security.core.dto;

/**
 * JWT(security context)로부터 차단 계정 정보를 옮기는 데 사용하는 객체
 */
public class BlockInfoDTO{
	
	/** 차단 시작 일자 */
	private java.util.Date blockStartDate;
	/** 차단 만료 일자 */
	private java.util.Date blockExpireDate;
	/** 차단 사유 */
	private String blockCause;
		
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
		return "BlockInfoDTO [blockStartDate=" + blockStartDate + ", blockExpireDate=" + blockExpireDate
				+ ", blockCause=" + blockCause + "]";
	}
}


