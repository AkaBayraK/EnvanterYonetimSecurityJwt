package com.service;

import com.entity.RoleEntity;

public interface RoleService {
	
    RoleEntity findRolByName(String name);

}