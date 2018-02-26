package com.suph.security.web.access.vote;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

public class CustomRoleVoter extends RoleVoter{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 입력된 문자열이 권한명으로 활용 가능한 형식인지 검증합니다.
	 */
	@Override
	public boolean supports(ConfigAttribute attribute) {
		if(		( StringUtils.hasText(attribute.getAttribute()) )
        	&&	  attribute.getAttribute().startsWith( getRolePrefix() )	// 약속된 접두어로 시작하고, null이 아니라면 true 반환
        ){
            return true;
        }else{
            return false;
        }
    }
	
	/**
	 * 요청자의 리소스 접근을 허용할지 판단한 결과를 메소드 호출자에게 반환합니다.
	 * 최종적인 판단은 AccessDecisionVoter들의 의견을 종합하여 AccessDecisionManager에서 내립니다.
	 * 
	 * @param authentication 접근 요청자의 정보를 지정합니다. 
	 * @param object 접근 요청된 경로를 지정합니다. 
	 * @param attributes 요청받은 경로 접근에 필요한 권한들을 지정합니다.
	 * @return result 1: 접근허가, 0: 판단보류, -1: 접근거부
	 */
	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes){
		logger.debug("리소스명 '{}'에 접근 가능한 권한 목록 '{}'", object, attributes);
		
		int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        logger.debug("접근 요청자가 가지고 있는 권한 목록 {}", authorities);
        
        // 해당 URL패턴에 접근 가능한 권한 목록들
        for(ConfigAttribute attribute : attributes){
            if( this.supports(attribute) ){
            // 각 권한명들이 올바른 패턴을 지니고 있다면(권한명으로 사용 가능한 이름이라면)
                result = ACCESS_DENIED;	// 일단 접근거부를 기본값으로 세팅하고 아래 절차를 수행합니다.
                
                // Attempt to find a matching granted authority
                // 접근을 요청한 사용자가 지닌 권한들과 일치하는게 있는지 찾아봅니다.
                for(GrantedAuthority authority : authorities){
                	
                	if( attribute.getAttribute().equals( authority.getAuthority() ) ){
                    	//logger.debug("(필요권한 {} - 가진권한 {}), ", attribute, authority);
                        return ACCESS_GRANTED;	// 하나라도 일치한다면 그 즉시 접근을 승인합니다.
                    }
                }
            }
        }

        return result;
    }
}
