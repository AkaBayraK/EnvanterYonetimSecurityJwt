package com.security.jwt;

import lombok.Data;

@Data
public class RefreshRequest {

	Long userId;
	String refreshToken;
}
