package com.suph.security;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("authDAO")
public interface AuthDAO{
	public abstract List<AuthVO> getAuthListById(String id);
}
