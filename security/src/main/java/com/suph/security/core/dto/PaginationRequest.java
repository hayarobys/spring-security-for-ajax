package com.suph.security.core.dto;

public class PaginationRequest{
	/** 요청 페이지 */
	private int pagenum;
	/** 한 페이지 당 항목 수 */
	private int pagesize;
	
	public int getStart(){
		return pagenum * pagesize;
	}

	public int getPagenum(){
		return pagenum;
	}

	public void setPagenum(int pagenum){
		this.pagenum = pagenum;
	}

	public int getPagesize(){
		return pagesize;
	}

	public void setPagesize(int pagesize){
		this.pagesize = pagesize;
	}
}
