package com.suph.security.web.toolonglogin;

import com.suph.security.core.userdetails.MemberInfo;

/**
 * 마지막 로그인으로부터 오랜시간 경과했을때 생성되는 예외입니다.
 * 개발자에게 알릴 수 있게 예외 사유가 저장되어야 합니다.
 * @author NB-0267
 *
 */
public class TooLongLoginException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	/** 장시간 로그인 한 계정의 정보를 담는 멤버 변수 입니다. */
	private MemberInfo memberInfo;
	
	public TooLongLoginException(String message, MemberInfo memberInfo){
		super(message);
		
		this.memberInfo = memberInfo;
	}

	/** 장시간 로그인 한 계정의 정보를 얻습니다 */
	public MemberInfo getMemberInfo(){
		return memberInfo;
	}
}
