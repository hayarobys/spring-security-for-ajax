/**
 * 특정 회원에게 특정 권한을 부여하는데 사용하는  DTO 
 */
package com.suph.security;

import java.util.ArrayList;

/**
 * 계정-권한 정보를 담는데 사용합니다.
 * @author NB-0267
 *
 */
public class MemberAuthVO{
	/** MEM_NO: 계정 일련 번호 */
	private String no;
	/** AUTH_NM: 부여할 권한 */
	private String auth;
}
