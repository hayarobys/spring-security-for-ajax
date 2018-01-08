package com.suph.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

public class CustomAffirmativeBased extends AffirmativeBased{
	public CustomAffirmativeBased(List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
	}
	
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException {
		logger.debug("접근 가능한 권한 목록: " + configAttributes);
		
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
