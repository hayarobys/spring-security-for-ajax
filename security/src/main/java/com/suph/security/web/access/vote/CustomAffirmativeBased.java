package com.suph.security.web.access.vote;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

public class CustomAffirmativeBased extends AffirmativeBased{
	/**
	 * 요청자가 요청한 리소스에 대해 인가 여부를 결정하는 클래스 입니다.
	 * 인가 여부를 결정하긴 위한 여러개의 AccessDecisionVoter들을 필요로 합니다.
	 * 보유한 AccessDecisionVoter들 중 단 하나라도 승인결정을 한다면 AffirmativeBased는 리소스 접근을 허용하도록 동작합니다. 
	 * @param decisionVoters
	 */
	public CustomAffirmativeBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
	}
	
	/**
	 * 보유한 AccessDecisionVoter들의 결정을 종합하여 요청에 대한 인가 여부를 결정합니다.
	 * @param authentication 요청자의 정보를 입력합니다. 요청자가 보유한 권한 목록을 구하는데 사용됩니다.
	 * @param contigAttributes 리소스 접근에 필요한 권한 목록을 입력합니다
	 */
	@Override
	public void decide(final Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException{
		
        int deny = 0;

        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);

            if (logger.isDebugEnabled()) {
                logger.debug("Voter: " + voter + ", returned: " + result);
            }

            switch (result) {
            case AccessDecisionVoter.ACCESS_GRANTED:
                return;

            case AccessDecisionVoter.ACCESS_DENIED:
                deny++;

                break;

            default:
                break;
            }
        }

        if (deny > 0) {
            throw new AccessDeniedException(
            		messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied")
            );
        }

        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }
}
