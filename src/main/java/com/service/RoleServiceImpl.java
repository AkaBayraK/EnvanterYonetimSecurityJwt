package com.service;

import org.springframework.stereotype.Service;

import com.entity.RoleEntity;
import com.hibernate.HibernateMySQLUtil;

@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public RoleEntity findRolByName(String name) {
    	RoleEntity result = new RoleEntity();
		try {
			result = HibernateMySQLUtil.loadEntityCriteria(RoleEntity.class, new Object[] { "name", name});				
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			e.printStackTrace();
		} finally {
		}
    	return result;
    }

}