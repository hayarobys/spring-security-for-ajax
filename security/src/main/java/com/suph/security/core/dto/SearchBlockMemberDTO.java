package com.suph.security.core.dto;

import java.util.Date;
import java.util.List;

public class SearchBlockMemberDTO{
	
	/** 검색 유형 (아이디/닉네임/일련 번호) */
	private String searchType;
	/** 검색어  */
	private String searchKeyword;
	/** 시작일 필터 */
	private Date blockStartDate;
	/** 만료일 필터 */
	private Date blockExpireDate;
	/** 검색 시간대(과거/현재/미래) */
	private List<String> searchTime;
	
	public String getSearchType(){
		return searchType;
	}
	
	public void setSearchType(String searchType){
		this.searchType = searchType;
	}
	
	public String getSearchKeyword(){
		return searchKeyword;
	}
	
	public void setSearchKeyword(String searchKeyword){
		this.searchKeyword = searchKeyword;
	}
	
	public Date getBlockStartDate(){
		return blockStartDate;
	}
	
	public void setBlockStartDate(Date blockStartDate){
		this.blockStartDate = blockStartDate;
	}
	
	public Date getBlockExpireDate(){
		return blockExpireDate;
	}
	
	public void setBlockExpireDate(Date blockExpireDate){
		this.blockExpireDate = blockExpireDate;
	}
	
	public List<String> getSearchTime(){
		return searchTime;
	}
	
	public void setSearchTime(List<String> searchTime){
		this.searchTime = searchTime;
	}

	@Override
	public String toString(){
		return "SearchBlockMemberDTO [searchType=" + searchType + ", searchKeyword=" + searchKeyword
				+ ", blockStartDate=" + blockStartDate + ", blockExpireDate=" + blockExpireDate + ", searchTime="
				+ searchTime + "]";
	}
}
