/*
 * 사용하지 않습니다.
 * 로그인 요청시 DB로부터 계정 정보를 불러오는 기능을 수행하는 클래스로서,
 * 현재는 LoginServiceImpl로 대체되었습니다.
 * 
 * 기본 세팅은 JDBC와 property로부터 계정정보를 읽어오도록 구현한 JdbcDaoImpl을 쓰지만,
 * 현재는 MyBatis + DB조회를 쓰는 LoginServiceImpl로 대체했습니다.
 */
package com.suph.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class CustomJdbcDaoImpl extends JdbcDaoImpl{
	// 로그 객체는 spring-jdbc > spring-tx > DaoSupport에 Log logger(getClass()) 형태로 존재
	// private static final Logger logger = Logger.getLogger(CustomJdbcDaoImpl.class);
	
	/**
	 * spring-orm > JdbcDaoImpl 클래스에 있는 메소드
	 * Users에 s가 붙어있음에 주의.
	 * DB로부터 해당 계정의 데이터들을 조회합니다.
	 * 
	 * @param userNo 검색할 계정의 일련 번호
	 * @return 연결된 DB로부터 해당 계정의 정보들을 찾아  T 자료형 리스트로 반환
	 */
	@Override
	protected List<UserDetails> loadUsersByUsername(final String userNo){
		// JdbcTemplate.query(String 쿼리, Object[] 인자, 반환값 담을 RowMapper<T>);
		return getJdbcTemplate().query(	// jdbcTemplate은 부모의 부모 클래스인 spring-jdbc > JdbcDaoSupport에 선언되어 있음.
				getUsersByUsernameQuery(),
				// "SELECT MEM_NO, MEM_ID, MEM_PASSWORD, MEM_NICKNAME FROM TB_MEMBER WHERE MEM_NO=?"
				new Integer[]{ Integer.parseInt(userNo) },
				new RowMapper<UserDetails>(){
						public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException{
							int no = rs.getInt(1);
							String username = rs.getString(2);
							String password = rs.getString(3);
							String name = rs.getString(4);
							char enable = rs.getString(5).charAt(0);
							
							// ID, 비밀번호, 이름은 DB값으로 채우지만, 권한 만큼은 일단 '권한 없음' 상태로 둔다. 이는 추후 아래쪽 코드에서 따로 조회하여 채울것이다.
							return new MemberInfo(no, username, password, name, enable, AuthorityUtils.NO_AUTHORITIES);
						}
				}
		);
	}
	
	/**
	 * spring-security-core > UserDetailService 인터페이스에 정의된 메서드
	 */
	@Override
	public UserDetails loadUserByUsername(final String userNo) throws UsernameNotFoundException{
		// ID로 DB조회
		List<UserDetails> users = loadUsersByUsername(userNo);
		
		// DB로부터 해당 ID와 일치하는 계정을 찾지 못했다면 예외 발생
		if(users.size() == 0){
			logger.debug("Query returned no results for user no '" + userNo + "'");
			
			UsernameNotFoundException ue = new UsernameNotFoundException(
					messages.getMessage("JdbcDaoImpl.notFound", new Object[]{ userNo },
					"UserNo {0} not found")
			);
			
			throw ue;
		}
		
		// 여러개 조회되었더라도 첫번째로 발견한 계정만 사용. 그러니 반드시 username(id)에 PK를 걸어 사용할 것.
		MemberInfo user = (MemberInfo)users.get(0);	// contains no GrantedAuthority[]
		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		
		// 이 계정의 권한 목록 반환
		if(getEnableAuthorities()){	// 권한 기능을 활성화 하지 않았다면 실행 X
			dbAuthsSet.addAll( loadUserAuthorities(user.getUsername()) );
		}
		
		// 이 계정의 그룹 명, 그룹 권한 목록 반환
		if(getEnableGroups()){	// 권한 그룹 기능을 활성화 하지 않았다면 실행 X
			dbAuthsSet.addAll( loadGroupAuthorities(user.getUsername()) );
		}
		
		// 이 계정의 세부 권한 + 그룹 권한을 Set -> List로 변환하여 계정 객체에 저장.
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		user.setAuthorities(dbAuths);
		
		// 만약 어떠한 권한도 조회되지 않았다면 익셉션 발생.
		if(dbAuths.size() == 0){
			// 이 계정에는 어떠한 권한도 없으며, '찾을 수 없음'으로 간주/처리 됩니다.
			logger.debug("User no '" + userNo + "' has no authorities and will be treated as 'not found'");
			UsernameNotFoundException ue = new UsernameNotFoundException(
					messages.getMessage(
							"JdbcDaoImpl.noAuthority", 
							new Object[] {userNo},
							"User {0} has no GrantedAuthority"
					)
			);
			
			throw ue;
		}
		
		return user;
	}
	
	/**
	 * DB의 권한 테이블에서 해당 계정의 권한 목록을 조회합니다.
	 * 
	 * @param userNo 조회할 계정 일련 번호
	 * @return 해당 계정의 권한 목록
	 */
	@Override
	protected List<GrantedAuthority> loadUserAuthorities(final String userNo){
		return getJdbcTemplate().query(
				getAuthoritiesByUsernameQuery(),
				// "SELECT AUTH_NM FROM TB_MEMBER_AUTH WHERE MEM_NO=?"
				new Integer[]{ Integer.parseInt(userNo) },
				new RowMapper<GrantedAuthority>(){
						public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException{
							String roleName = getRolePrefix() + rs.getString(1);
							
							return new SimpleGrantedAuthority(roleName);
						}
				}
		);
	}
	
	/**
	 * 해당 계정이 속한 그룹의 그룹명, 그룹 권한 목록 반환
	 * 
	 * @param userNo 그룹 권한 정보를 조회하고 싶은 계정의 일련 번호
	 * @return 해당 계정의 그룹 ID, 그룹명, 그룹 권한 목록 반환
	 */
	@Override
	protected List<GrantedAuthority> loadGroupAuthorities(final String userNo){
		return super.loadGroupAuthorities(userNo);
		//"select g.id, g.group_name, ga.authority " +
        //"from groups g, group_members gm, group_authorities ga " +
        //"where gm.username = ? " +
        //"and g.id = ga.group_id " +
        //"and g.id = gm.group_id";
	}
}







