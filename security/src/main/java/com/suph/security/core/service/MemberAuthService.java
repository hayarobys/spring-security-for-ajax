package com.suph.security.core.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface MemberAuthService{
	public List<GrantedAuthority> loadUserAuthorities(int memNo);
}
