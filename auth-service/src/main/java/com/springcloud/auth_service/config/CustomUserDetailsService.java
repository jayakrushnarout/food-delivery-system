package com.springcloud.auth_service.config;

import com.springcloud.auth_service.entity.User;
import com.springcloud.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService
{

	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		// TODO Auto-generated method stub
		User user=repo.findByUsername(username).get();
		if(user!=null)
		{
			
			List<SimpleGrantedAuthority> authorities=new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

			CustomUserDetails userdetails=new CustomUserDetails(user,authorities);
			
			return userdetails;
		}
		else
		{
			throw new UsernameNotFoundException("invalid Username");
		}
	}

}
