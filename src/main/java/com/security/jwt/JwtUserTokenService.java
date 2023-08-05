package com.security.jwt;


import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.entity.UserEntity;
import com.entity.UserTokenEntity;
import com.service.UserTokenServiceImpl;

@Service
public class JwtUserTokenService {
	
	@Value("${kafein.app.jwtExpirationMs}")
	Long jwtExpirationMs;
	
	@Autowired
	private UserTokenServiceImpl userTokenServiceImpl;

	
	public String createRefreshToken(UserEntity user, String accessToken) {
		UserTokenEntity token = userTokenServiceImpl.getById(user.getId());
		if(token == null) {
			token =	new UserTokenEntity();
			token.setUser(user);
		}
		token.setToken(accessToken); //UUID.randomUUID().toString());
		token.setExpiryDate(Date.from(Instant.now().plusSeconds(jwtExpirationMs)));
		
		userTokenServiceImpl.save(token);
		
		return token.getToken();
	}
	
	public boolean isRefreshExpired(UserTokenEntity token) {
		return token.getExpiryDate().before(new Date());
	}

	public UserTokenEntity getByUser(Long userId) {
		return userTokenServiceImpl.getById(userId);	
	}

}
