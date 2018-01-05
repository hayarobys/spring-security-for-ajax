package com.suph.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public String encode(CharSequence rawPassword) {
        String result = super.encode(rawPassword);
        logger.debug("encode \"{}\" to \"{}\"", rawPassword, result);
        return result;
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean result = super.matches(rawPassword, encodedPassword);
        logger.debug("matches rawPassword: {}, encodedPassword: {}, result: {}", new Object[]{rawPassword, encodedPassword, result});
        return result;
    }
}
