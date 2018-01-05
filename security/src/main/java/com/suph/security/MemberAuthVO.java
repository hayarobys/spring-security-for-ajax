/**
 * 특정 회원에게 특정 권한을 부여하는데 사용하는  DTO 
 */
package com.suph.security;

import java.util.ArrayList;

public class MemberAuthVO{
	private String id;		// MEM_ID: 부여할 회원
	private String auth;	// AUTH_NM; 부여할 권한
	
}
