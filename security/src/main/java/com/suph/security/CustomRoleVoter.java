package com.suph.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomRoleVoter extends RoleVoter{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/*
	 * 입력된 문자열이 권한명으로 활용 가능한 정보인지 검증합니다.
	 */
	@Override
	public boolean supports(ConfigAttribute attribute) {
		logger.debug("attribute.getAttribute() = {}", attribute.getAttribute());
		logger.debug("getRolePrefix() = {}", getRolePrefix());
		logger.debug("attribute.getAttribute().startsWith( getRolePrefix() ) = {}", attribute.getAttribute().startsWith( getRolePrefix() ));
        if(		( attribute.getAttribute() != null )
        	&&	  attribute.getAttribute().startsWith( getRolePrefix() )	// 약속된 접두어로 시작하고, null이 아니라면 true 반환
        ){
        	logger.debug("사용가능한 권한명입니다.");
            return true;
        }else{
        	logger.debug("사용 할 수 없는 권한명입니다.");
            return false;
        }
    }
	
	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes){
		logger.debug("접근 가능한 권한 목록 {}", attributes);
		
		int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        // 해당 URL패턴에 접근 가능한 권한 목록들
        for(ConfigAttribute attribute : attributes){
            if( this.supports(attribute) ){
            // 각 권한명들이 올바른 패턴을 지니고 있다면(권한명으로 사용 가능한 이름이라면)
                result = ACCESS_DENIED;	// 일단 접근거부를 기본값으로 세팅하고 아래 절차를 수행합니다.
                
                // Attempt to find a matching granted authority
                // 접근을 요청한 사용자가 지닌 권한들과 일치하는게 있는지 찾아봅니다.
                for(GrantedAuthority authority : authorities){
                	
                	logger.debug("authority.getAuthority() = {}", authority.getAuthority());
                	logger.debug("attribute.getAttribute().equals( authority.getAuthority() ) = {}", attribute.getAttribute().equals( authority.getAuthority() ));
                    if( attribute.getAttribute().equals( authority.getAuthority() ) ){
                    	logger.debug("(필요권한 {} - 가진권한 {}), ", attribute, authority);
                        return ACCESS_GRANTED;	// 하나라도 일치한다면 그 즉시 접근을 승인합니다.
                    }
                }
            }
        }

        return result;
    }
}
