
/**
 * 계정 정보 객체
 */
package com.suph.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class MemberInfo implements UserDetails{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private int no;				// MEM_NO 계정 일련 번호 PK
	private String id;			// MEM_ID 계정 아이디
	private String password;	// MEM_PASSWORD 계정 비밀번호
	private String name;		// MEM_NICKNM 계정 사용자의 이름 또는 별명
	private char enable;		// MEM_ENABLE 계정 사용 여부(탈퇴 여부). Y는 사용 중, N은 탈퇴 혹은 휴면 계정. 
	
	private Set<GrantedAuthority> authorities;	// 계정이 가지고 있는 권한 목록
	
	
	// 디폴트 생성자 미정의시 mybatis가 이용하는 자바의 reflection > ObjectFactory > create에서 디폴트 생성자를 찾지 못했다고 익셉션을 낸다.
	private MemberInfo(){}	// 디폴트 생성자
	// private이더라도 mybatis는 Reflection클래스에서 constructor.setAccessible(true); 로 타깃 클래스들의 생성자 접근 옵션을 접근 가능으로 바꾸기에
	// 어떠한 접근제한자 이더라도 mybatis는 접근할 수 있다.
	
	/*public static MemberInfo createInstance(){
		MemberInfo memberInfo = new MemberInfo();
		return memberInfo;
	}*/
	
	public MemberInfo(
			int no, String id, String password, String name, char enable,
			Collection<? extends GrantedAuthority> authorities
	){
		this.no = no;
		this.id = id;
		this.password = password;
		this.name = name;
		this.enable = enable;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}
		
	/**
	 * 계정 일련 번호를 반환합니다.
	 * @return no 반환
	 */
	public int getNo(){
		return no;
	}

	/**
	 * 계정 일련 번호를 변경/저장 합니다.
	 * @param no
	 */
	public void setNo(int no){
		this.no = no;
	}

	/**
	 * 계정 ID를 반환합니다.
	 * @return id 반환
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * 계정의 ID를 반환합니다.
	 * @return id 반환. 내부적으로 getId()를 호출하여 반환합니다.
	 */
	@Override
	public String getUsername(){
		return getId();
	}

	/**
	 * 계정 ID를 변경/저장 합니다.
	 * @param id
	 */
	public void setId(String id){
		logger.debug("setNoId() = {}", id);
		this.id = id;
	}

	/**
	 * 계정의 비밀번호를 반환합니다.
	 * @return password 반환
	 */
	@Override
	public String getPassword(){
		return password;
	}

	/**
	 * 계정 비밀번호를 변경/저장 합니다.
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * 계정 사용자의 이름을 반환합니다.
	 * @return name 반환
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 계정 사용자의 이름을 변경/저장 합니다.
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 계정 사용 여부를 반환 합니다.
	 * @return enable 반환
	 */
	public char getEnable(){
		return enable;
	}

	/**
	 * 계정 사용 여부를 변경/저장 합니다.
	 * @param enable 사용시 Y, 탈퇴 혹은 휴면 계정일 경우 N
	 */
	public void setEnable(char enable){
		this.enable = enable;
	}
	
	/**
	 * 계정의 권한 목록을 반환 합니다.
	 * @return 권한 목록을 Collection 형태로 반환 합니다. 내장된 기본 자료형은 Set 입니다.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return authorities;
	}
	
	/**
	 * 계정의 권한 목록을 변경/저장 합니다.
	 * @param authorities
	 */
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities){
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}
	
	/**
	 * 계정이 만료되지 않았는지를 반환합니다.(true를 리턴하면 만료되지 않음을 의미)
	 * @return 만료 여부 boolean값 반환
	 */
	@Override
	public boolean isAccountNonExpired(){
		return true;
	}
	
	/**
	 * 계정이 잠겨있지 않은지를 반환합니다.(true를 리턴하면 계정이 잠겨있지 않음을 의미)
	 * @return 잠김 여부 boolean값 반환
	 */
	@Override
	public boolean isAccountNonLocked(){
		return true;
	}
	
	/**
	 * 계정의 패스워드가 만료되지 않았는지를 반환합니다.(true를 리턴하면 패스워드가 만료되지 않음을 의미)
	 * @return 패스워드 만료 여부 boolean값 반환
	 */
	@Override
	public boolean isCredentialsNonExpired(){
		return true;
	}
	
	/**
	 * 계정이 사용가능한 계정인지를 반환합니다.(true를 리턴하면 사용가능한 계정임을 의미)
	 * @return 계정 사용 가능 여부 boolean값 반환
	 */
	@Override
	public boolean isEnabled(){
		return true;
	}
	
	/**
	 * 입력되는 권한 목록(Collection)을 정렬하여 반환합니다.
	 * @param authorities GrantedAuthority를 상속받는 권한 객체들의 Collection
	 * @return 정렬한 Collection 반환
	 */
	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities){
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(new AuthorityComparator());
		
		for(GrantedAuthority grantedAuthority : authorities){
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}
		
		return sortedAuthorities;
	}
	
	/**
	 * 정렬 알고리즘.
	 * 다른 객체의 권한이 null이면 음수, 원본 객체의 권한이 null이면 양수 반환.
	 * 둘 다 권한을 가진 정상 객체일 경우 두 GrantedAuthority객체의  권한에 대한 정렬 실시
	 */
	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable{
		private static final long serailVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
		
		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2){
			if(g2.getAuthority() == null) {
				return -1;
			}
			
			if(g1.getAuthority() == null) {
				return 1;
			}
			
			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}

	@Override
	public String toString(){
		return "MemberInfo [no=" + no + ", id=" + id + ", password=" + password + ", name=" + name + ", enable="
				+ enable + ", authorities=" + authorities + "]";
	}
}





















