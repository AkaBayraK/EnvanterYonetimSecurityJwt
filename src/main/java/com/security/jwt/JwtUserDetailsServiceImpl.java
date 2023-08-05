package com.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.entity.UserEntity;
import com.service.UserServiceImpl;

import jakarta.persistence.Transient;


@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserServiceImpl userServiceImpl;

	@Override
	@Transient
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userServiceImpl.findUserByUserName(username);
		return JwtUserDetails.build(user);
	}
  
	public UserDetails loadUserById(Long id) {
		UserEntity user = userServiceImpl.getById(id);
		return JwtUserDetails.build(user); 
	}
  
}
