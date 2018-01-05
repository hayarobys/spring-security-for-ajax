package com.suph.security;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberDAO{
	public abstract int insertMemverVO(MemberInfo vo);
}
