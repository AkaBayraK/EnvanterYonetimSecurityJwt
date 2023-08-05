package com.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
	
	String message;
	Long userId;
	String userName;
	String accessToken;
	String refreshToken;

}
