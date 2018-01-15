package com.suph.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder{
	private static final Logger logger = LoggerFactory.getLogger(CustomBCryptPasswordEncoder.class);
	
	public String encode(CharSequence rawPassword) {
        String result = super.encode(rawPassword);
        return result;
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean result = super.matches(rawPassword, encodedPassword);
        return result;
    }
}
