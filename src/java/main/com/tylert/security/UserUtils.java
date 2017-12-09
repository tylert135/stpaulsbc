package com.tylert.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.tylert.domain.ViewItem;

public class UserUtils {

	public static boolean isInRole(List<String> authorities, String inRole)
	{
		for (String authority : authorities)
		{
			// Super user gets all and anything...
	    	if(authority.equals(inRole) || ViewItem.SUPER_ROLE.equals(authority))
	    		return true;
		}
		return false;
	}
	
	public static String getUsername()
	{
		SecurityContext context = SecurityContextHolder.getContext();		
		Authentication authentication = context.getAuthentication();
		return  authentication.getName();
	}
	
	public static List<String> getRoles ()
	
	{
		List<String> roleList = new ArrayList<String>();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication.getPrincipal() instanceof User )
		{
			User _user = (User) authentication.getPrincipal();
			for (GrantedAuthority ga : _user.getAuthorities())
				roleList.add(ga.getAuthority());
			return roleList;
		}
		roleList.add((String)authentication.getPrincipal());
		return roleList;
	}
	
	public static boolean isUserAuthorizedForView(ViewItem vi, List<String> authorities)
	{
		for (String role : vi.getRoles())
		{
			if(isInRole(authorities, role))
				return true;
		}
		return false;
	}
}
