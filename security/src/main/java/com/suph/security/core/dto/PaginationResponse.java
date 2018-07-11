package com.suph.security.core.dto;

import java.util.List;

public class PaginationResponse<T>{
	/** 데이터 목록 */
	private List<T> rows;
	/** DB상에 존재하는 전체 데이터 개수 (페이징 처리와 무관하게) */
	private int totalRows;
	
	public PaginationResponse(List<T> rows, int totalRows){
		this.rows = rows;
		this.totalRows = totalRows;
	}
	
	public List<T> getRows(){
		return rows;
	}
	
	public void setRows(List<T> rows){
		this.rows = rows;
	}
	
	public int getTotalRows(){
		return totalRows;
	}
	
	public void setTotalRows(int totalRows){
		this.totalRows = totalRows;
	}
}
