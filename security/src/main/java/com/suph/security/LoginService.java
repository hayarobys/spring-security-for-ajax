package com.suph.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginService extends UserDetailsService{

	/**
	 * ID와 일치하는 계정의 정보를 조회하는 메서드
	 * @param id 조회할 계정 ID 
	 */
	public abstract UserDetails loadUserByUsername(String id) throws UsernameNotFoundException;
}
