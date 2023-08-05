package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dto.UserDto;
import com.entity.EnvanterEntity;
import com.entity.UserEntity;
import com.service.DepoServiceImpl;
import com.service.EnvanterServiceImpl;
import com.service.KategoriServiceImpl;
import com.service.UrunServiceImpl;
import com.service.UserServiceImpl;

@RestController
@RequestMapping("/api/home")
public class AAuthController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;	
	@Autowired
    private EnvanterServiceImpl EnvanterServiceImpl;
	@Autowired
    private DepoServiceImpl DepoServiceImpl;
	@Autowired
    private UrunServiceImpl UrunServiceImpl;
	@Autowired
    private KategoriServiceImpl KategoriServiceImpl;
	
	
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
    
    @PostMapping("/register/save")
    public ModelAndView registration(@ModelAttribute("user") UserDto userDto, BindingResult result) {
    	ModelAndView modelAndView = new ModelAndView("register?success"); //buan abk sayafa gitmiyor logine atıyor
    	
        UserEntity existingUser = userServiceImpl.findUserByUserName(userDto.getUserName());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "Kayıtlı mail bilgisi mevcut. Login sayfasın girişi deniyiniz.");
        }

        if(result.hasErrors()){
        	modelAndView = new ModelAndView("register");
            UserDto user = new UserDto();
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        userServiceImpl.saveUser(userDto);
        
        return modelAndView;
    }
    
    @GetMapping("/api/envanterler")
    public ModelAndView showFormForList() {
    	ModelAndView modelAndView = new ModelAndView("envanterlistesi");
//    	SearchEnvanterDTO dto = new SearchEnvanterDTO();
//    	modelAndView.addObject("searchEnvanterDTO", dto);
    	EnvanterEntity en = new EnvanterEntity();
    	modelAndView.addObject("searchEnvanter", en);
    	modelAndView.addObject("listdepo", DepoServiceImpl.getAll());
    	modelAndView.addObject("listurun", UrunServiceImpl.getAll());
    	modelAndView.addObject("kategorilist", KategoriServiceImpl.getAll());
    	modelAndView.addObject("envanterlist", EnvanterServiceImpl.getAll());
		return modelAndView;
    }
    
}