
/**
 * 계정 정보 객체
 */
package com.suph.security.core.userdetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.suph.security.core.dto.BlockInfoDTO;

public class MemberInfo implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** MEM_NO 계정 일련 번호 PK */
	private int no;
	/** MEM_ID 계정 아이디 */
	private String id;
	/** MEM_PASSWORD 계정 비밀번호 */
	private transient String password;
	/** MEM_NM 계정 닉네임 */
	private String name;
	/** MEM_ENABLE 계정 사용 여부(탈퇴 여부). Y는 사용 중, N은 탈퇴 혹은 휴면 계정. */
	private boolean enable; 
	/** 계정이 가지고 있는 권한 목록 */
	private Set<GrantedAuthority> authorities;
	/** 계정 차단 정보. null이라면 미차단 계정. */
	private List<BlockInfoDTO> blockInfo;
	/** 로그인 토큰(JWT) 생성일 */
	private Date issuedAt;
	
	// 디폴트 생성자 미정의시 mybatis가 이용하는 자바의 reflection > ObjectFactory > create에서 디폴트 생성자를 찾지 못했다고 익셉션을 낸다.
	//private MemberInfo(){}	// 디폴트 생성자
	// private이더라도 mybatis는 Reflection클래스에서 constructor.setAccessible(true); 로 타깃 클래스들의 생성자 접근 옵션을 접근 가능으로 바꾸기에
	// 어떠한 접근제한자 이더라도 mybatis는 접근할 수 있다.
	
	public MemberInfo(
			int no,
			String id,
			String password,
			String name,
			Collection<? extends GrantedAuthority> authorities,
			List<BlockInfoDTO> blockInfo,
			Date issuedAt
	){
		this(no, id, password, name, true, authorities, blockInfo, issuedAt);
	}
	
	public MemberInfo(
			int no,
			String id,
			String password,
			String name,
			boolean enable,
			Collection<? extends GrantedAuthority> authorities,
			List<BlockInfoDTO> blockInfo
	){
		this(no, id, password, name, enable, authorities, blockInfo, null);
	}
	
	public MemberInfo(
			int no,
			String id,
			String password,
			String name,
			boolean enable,
			Collection<? extends GrantedAuthority> authorities,
			List<BlockInfoDTO> blockInfo,
			Date issuedAt
	){
		this.no = no;
		this.id = id;
		this.password = password;
		this.name = name;
		this.enable = enable;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
		this.blockInfo = blockInfo;
		this.issuedAt = issuedAt;
	}
	
	/**
	 * 계정의 일련 번호를 문자열로 반환합니다.
	 * @return mem_no 반환. 내부적으로 계정 일련번호 no를 문자열로 변환하여 반환합니다.
	 */
	@Override
	public String getUsername(){
		return Integer.toString(no);
	}
	
	/**
	 * 계정의 일련 번호를 int형으로 반환합니다.
	 * 내부적으로 getUsername()을 호출합니다.
	 * @return
	 */
	public int getNo(){
		return Integer.parseInt(getUsername());
	}
	
	/**
	 * 계정의 아이디를 반환합니다.
	 * @return
	 */
	public String getId(){
		return id;
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
	 * 계정의 닉네임을 반환합니다.
	 * @return
	 */
	public String getName(){
		return name;
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
	 * 계정의 차단 정보를 반환 합니다.
	 * @return
	 */
	public List<BlockInfoDTO> getBlockInfo(){
		return blockInfo;
	}
	
	/**
	 * 로그인 토큰(JWT) 생성 일 을 반환 합니다.
	 * @return
	 */
	public Date getIssuedAt(){
		return this.issuedAt;
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
	 * 계정의 차단 유무를 반환합니다.
	 * @return 잠긴 계정이라면 false, 열린 계정이라면 true 반환
	 */
	@Override
	public boolean isAccountNonLocked(){
		//return this.blockInfo == null;
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
		return this.enable;
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
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

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
}





















