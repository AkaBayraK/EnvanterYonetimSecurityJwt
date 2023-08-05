package com.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
	
	  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	  @Value("${kafein.app.jwtSecret}")
	  private String jwtSecret;

	  @Value("${kafein.app.jwtExpirationMs}")
	  private int jwtExpirationMs;

	  @Value("${kafein.app.jwtCookieName}")
	  private String jwtCookie;

	    public String generateJwtToken(Authentication authentication){
	        String username = authentication.getName();

	        Date currentDate = new Date();

	        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMs);

	        String token = Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(new Date())
	                .setExpiration(expireDate)
	                .signWith(key())
	                .compact();
	        return token;
	    }
		
	  public String getJwtFromCookies(HttpServletRequest request) {
	    Cookie cookie = WebUtils.getCookie(request, jwtSecret);
	    if (cookie != null) {
	      return cookie.getValue();
	    } else {
	      return null;
	    }
	  }

	  public ResponseCookie generateJwtCookie(JwtUserDetails userPrincipal) {
	    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
	    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
	    return cookie;
	  }

	  public ResponseCookie getCleanJwtCookie() {
	    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
	    return cookie;
	  }

	  public String getUserNameFromJwtToken(String token) {
	    return Jwts.parserBuilder().setSigningKey(key()).build()
	               .parseClaimsJws(token).getBody().getSubject();
	  }

	  private Key key() {
	    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	  }

	  public boolean validateJwtToken(String authToken) {
	    try {
	      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
	      return true;
	    } catch (MalformedJwtException e) {
	    	e.printStackTrace();
	      logger.error("Invalid JWT token: {}", e.getMessage());
	    } catch (ExpiredJwtException e) {
	    	e.printStackTrace();
	      logger.error("JWT token is expired: {}", e.getMessage());
	    } catch (UnsupportedJwtException e) {
	    	e.printStackTrace();
	      logger.error("JWT token is unsupported: {}", e.getMessage());
	    } catch (IllegalArgumentException e) {
	    	e.printStackTrace();
	      logger.error("JWT claims string is empty: {}", e.getMessage());
	    }

	    return false;
	  }

	  public String generateTokenFromUsername(String username) {   
	    return Jwts.builder()
	               .setSubject(username)
	               .setIssuedAt(new Date())
	               .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
	               .signWith(key(), SignatureAlgorithm.HS256)
	               .compact();
	  }
	  
	    public String getUsername(String token){
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        String username = claims.getSubject();
	        return username;
	    }
	    

}