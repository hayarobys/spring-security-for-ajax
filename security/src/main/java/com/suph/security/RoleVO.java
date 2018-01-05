/**
 * TB_RESOURCE_AUTH의 정보를 담는데 사용 
 */
package com.suph.security;

public class RoleVO{
	private String pattern;
	private String authority;
	
	public String getPattern(){
		return pattern;
	}

	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	public String getAuthority(){
		return authority;
	}
	
	public void setAuthority(String authority){
		this.authority = authority;
	}
}
