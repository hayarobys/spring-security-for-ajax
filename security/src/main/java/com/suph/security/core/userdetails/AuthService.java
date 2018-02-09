package com.suph.security.core.userdetails;

import java.util.Map;

public interface AuthService{

	/**
	 * 모든 권한 목록을 조회합니다.
	 * @return
	 */
	public abstract Map<String, Object> getAuthList();
}
