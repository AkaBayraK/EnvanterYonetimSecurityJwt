package com.security.jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtUserDetails implements UserDetails {

	private static final long serialVersionUID = -1248928038530372924L;
	
	public Long id;
	private String username;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
    public JwtUserDetails(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
    	    this.id = id;
    	    this.username = username;
    	    this.email = email;
    	    this.password = password;
    	    this.authorities = authorities;
    } 

    public static JwtUserDetails build(UserEntity user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role. getRole().getName())).collect(Collectors.toList());
        return new JwtUserDetails(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), authorities);
      }
    
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
