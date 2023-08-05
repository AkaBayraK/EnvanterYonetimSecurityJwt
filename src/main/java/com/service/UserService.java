package com.service;

import java.util.List;

import com.dto.UserDto;
import com.entity.UserEntity;

public interface UserService {
	
	UserEntity saveUser(UserDto userDto);

	UserEntity getById(Long id);
    UserEntity findUserByUserName(String userName);

    List<UserDto> findAllUsers();
}