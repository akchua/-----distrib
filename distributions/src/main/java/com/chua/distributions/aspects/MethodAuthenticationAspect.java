package com.chua.distributions.aspects;

import javax.ws.rs.NotAuthorizedException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Feb 2017
 */
@Aspect
public class MethodAuthenticationAspect {

	@Before("@annotation(checkAuthority) && execution(* *(..))")
	public void checkUserAuthority(CheckAuthority checkAuthority) throws Exception {
		final Integer userAuthority = UserContextHolder.getUser().getUserType().getAuthority();
		if(userAuthority > checkAuthority.minimumAuthority() || userAuthority < checkAuthority.maximumAuthority()) {
			throw new NotAuthorizedException("User is not authenticated.");
		}
	}
}
