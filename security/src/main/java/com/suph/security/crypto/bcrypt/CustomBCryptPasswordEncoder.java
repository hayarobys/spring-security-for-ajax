package com.suph.security.crypto.bcrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder{
	private static final Logger logger = LoggerFactory.getLogger(CustomBCryptPasswordEncoder.class);
	
	/**
	 * 비밀번호를 BCrypt방식으로 암호화 합니다.
	 */
	public String encode(CharSequence rawPassword) {
        String result = super.encode(rawPassword);
        return result;
    }

	/**
	 * 두 비밀번호의 일치여부를 비교합니다.
	 * @param rawPassword 평문 비밀번호를 입력합니다. 주로 로그인 요청한 비밀번호를 입력합니다.
	 * @param encodedPassword 암호화된 비밀번호를 입력합니다. 주로 DB에 저장된 비밀번호를 입력합니다.
	 * @return result 일치하면 true, 불일치 시 false를 반환합니다.
	 */
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean result = super.matches(rawPassword, encodedPassword);
        return result;
    }
}
