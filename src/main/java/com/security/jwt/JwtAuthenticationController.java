package com.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dto.UserDto;
import com.entity.UserEntity;
import com.entity.UserTokenEntity;
import com.service.UserServiceImpl;


@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {
	
	private AuthenticationManager authenticationManager;
	
	private JwtTokenProvider jwtTokenProvider;
	
	private UserServiceImpl userService;
	
	private JwtUserTokenService refreshTokenService;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
	private UserServiceImpl userServiceImpl;
	
    public JwtAuthenticationController(
    		AuthenticationManager authenticationManager, 
    		UserServiceImpl userService, 
    		JwtTokenProvider jwtTokenProvider, 
    		JwtUserTokenService refreshTokenService
    		) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }
    
    @GetMapping("/index")
    public ModelAndView index() {
    	ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
    }
    @GetMapping("/register")
    public ModelAndView register() {
    	ModelAndView modelAndView = new ModelAndView("register");
        UserDto user = new UserDto();
        modelAndView.addObject("user", user);
		return modelAndView;
    }
    
    @GetMapping("/login")
    public ModelAndView login	() {
    	ModelAndView modelAndView = new ModelAndView("login");
        UserDto user = new UserDto();
        modelAndView.addObject("user", user);
		return modelAndView;
    }
    
    @PostMapping("/dashboard")
    public ModelAndView dashboard(@ModelAttribute("user") UserDto userDto, BindingResult result) {
       	ModelAndView modelAndView = new ModelAndView("index");   	
    	UserEntity existingUser = userServiceImpl.findUserByUserName(userDto.getUserName());
    	if(existingUser != null && existingUser.getUserName() != null && !existingUser.getUserName().isEmpty()){      	
    		modelAndView = new ModelAndView("dashboard");
        } else {
        	modelAndView = new ModelAndView("login");
        }
		return modelAndView;
    }    
    
    
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody UserDto loginRequest) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.generateJwtToken(auth);
		
		UserEntity user =  userService.findUserByUserName(loginRequest.getUserName());
		
		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setAccessToken("Bearer " + jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user, jwtToken));
		authResponse.setUserId(user.getId());
		return authResponse;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDto registerRequest) {
		AuthenticationResponse authResponse = new AuthenticationResponse();
		
		UserEntity user =  userService.findUserByUserName(registerRequest.getUserName());
		if(user != null) {
			authResponse.setMessage("UserName kullanicisi mevcuttur.");
			return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
		} else {
			UserDto userDto = new UserDto();
			userDto.setUserName(registerRequest.getUserName());
			userDto.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			user = userService.saveUser(userDto);
		}
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUserName(), registerRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.generateJwtToken(auth);
		
		authResponse.setMessage("User  başarılı kaydedildi.");
		authResponse.setAccessToken("Bearer " + jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user, jwtToken));
		authResponse.setUserId(user.getId());
		authResponse.setUserName(user.getUserName());
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);		
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
		AuthenticationResponse response = new AuthenticationResponse();
		UserTokenEntity token = refreshTokenService.getByUser(refreshRequest.getUserId());
		if(token==null 
				|| (//token.getToken().equals(refreshRequest.getRefreshToken()) && /*daha once aldigi token bilinmiyor olabilir*/
						!refreshTokenService.isRefreshExpired(token)
						)
				) {
			UserEntity user = null;
			if (token == null) {
				user = userService.getById(refreshRequest.getUserId());
			} else {
				user = token.getUser();
			}
			if (user==null || (user!=null && user.getId()==null)) {
				response.setMessage(refreshRequest.getUserId() + " user is not valid.");
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);				
			}
			String jwtToken = jwtTokenProvider.generateTokenFromUsername(user.getUserName());
			response.setMessage("token successfully refreshed.");
			response.setAccessToken("Bearer " + jwtToken);
			response.setUserId(user.getId());
			return new ResponseEntity<>(response, HttpStatus.OK);		
		} else {
			response.setMessage("refresh token is not valid.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		
	}
	

}
