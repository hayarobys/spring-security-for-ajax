package com.suph.security.core.dto;

public class HttpMethodDTO{
	/** HTTP 메소드 일련 번호 */
	private int httpMethodNo;
	/** HTTP 메소드 패턴(GET / DELETE / POST / PATCH / PUT / ...) */
	private String httpMethodPattern;
	/** HTTP 메소드 설명 */
	private String httpMethodExplanation;
	
	public int getHttpMethodNo(){
		return httpMethodNo;
	}
	
	public void setHttpMethodNo(int httpMethodNo){
		this.httpMethodNo = httpMethodNo;
	}
	
	public String getHttpMethodPattern(){
		return httpMethodPattern;
	}
	
	public void setHttpMethodPattern(String httpMethodPattern){
		this.httpMethodPattern = httpMethodPattern;
	}
	
	public String getHttpMethodExplanation(){
		return httpMethodExplanation;
	}
	
	public void setHttpMethodExplanation(String httpMethodExplanation){
		this.httpMethodExplanation = httpMethodExplanation;
	}
}
