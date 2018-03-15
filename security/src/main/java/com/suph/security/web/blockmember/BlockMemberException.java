package com.suph.security.web.blockmember;

import com.suph.security.core.userdetails.MemberInfo;

public class BlockMemberException extends RuntimeException{
	/** 차단된 계정의 정보를 담는 멤버 변수 입니다. */
	private MemberInfo memberInfo;
	
	public BlockMemberException(String message, MemberInfo memberInfo){
		super(message);
		this.memberInfo = memberInfo;
	}
	
	/** 장시간 로그인 한 계정의 정보를 얻습니다 */
	public MemberInfo getMemberInfo(){
		return memberInfo;
	}
}
