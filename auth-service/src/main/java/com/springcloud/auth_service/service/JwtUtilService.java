package com.springcloud.auth_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;

@Service
public class JwtUtilService 
{
	@Value("${spring.app.jwtSecret}")//Base64 encoded secret key
	public String secretKey;
	
	@Value("${spring.app.jwtExpirationMs}")
	public long expiration;
	
    private static final Logger logger = LoggerFactory.getLogger(JwtUtilService.class);

	public String generateJwtToken(UserDetails user)
	{
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> role = user.getAuthorities();

		List<String> authorityList = role.stream()
		    .map(auth->auth.getAuthority())
		    .toList(); // You can use .collect(Collectors.toList()) if you're using Java 8

		claims.put("role", authorityList);
		
	    String token = Jwts.builder()
		.subject(user.getUsername())
		.addClaims(claims)//add role and privileges
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis()+expiration))
		.signWith(getKey())
		.compact();
	    return token;
	}
	public Key getKey()
	{
		
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		System.out.println(Arrays.toString(Base64.getDecoder().decode(secretKey)));

		return Keys.hmacShaKeyFor(keyBytes);
		
	}

}


