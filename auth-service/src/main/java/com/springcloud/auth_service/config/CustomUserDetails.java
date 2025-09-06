package com.springcloud.auth_service.config;

import com.springcloud.auth_service.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails
{
	
	private User user;
	
	private List<SimpleGrantedAuthority> authorities;
	
	

	public CustomUserDetails(User user, List<SimpleGrantedAuthority> list) {
		super();
		this.user = user;
		this.authorities = list;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() 
	{
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() 
	{
		// TODO Auto-generated method stub
		return user.getUsername();
	}

}
