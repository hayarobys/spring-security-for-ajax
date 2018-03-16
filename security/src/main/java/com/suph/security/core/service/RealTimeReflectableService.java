package com.suph.security.core.service;

import java.util.Map;

public interface RealTimeReflectableService{
	/**
	 * 서버 재부팅 없이 DB 데이터로 서버 메모리를 실시간 갱신 합니다.
	 * @throws Exception
	 */
	public abstract Map<String, Object> reload();
}
