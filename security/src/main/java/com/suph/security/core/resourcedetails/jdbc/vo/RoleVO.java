/**
 * TB_RESOURCE_AUTH의 정보를 담는데 사용 
 */
package com.suph.security.core.resourcedetails.jdbc.vo;

/**
 * 리소스-권한 정보를 담는데 사용됩니다.
 * (리소스 접근에 필요한 권한 정보)
 * @author NB-0267
 *
 */
public class RoleVO{
	/** URL 패턴 */
	private String pattern;
	/** 접근 가능한 권한(필요권한) */
	private String authority;
	
	/**
	 * URL패턴을 반환 합니다.
	 * @return
	 */
	public String getPattern(){
		return pattern;
	}

	/**
	 * URL패턴을 저장/변경 합니다.
	 * @param pattern
	 */
	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	/**
	 * 필요권한을 반환 합니다.
	 * @return
	 */
	public String getAuthority(){
		return authority;
	}
	
	/**
	 * 필요 권한을 저장/변경 합니다.
	 * @param authority
	 */
	public void setAuthority(String authority){
		this.authority = authority;
	}

	@Override
	public String toString(){
		return "RoleVO [pattern=" + pattern + ", authority=" + authority + "]";
	}
}
