package com.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.UserDto;
import com.entity.RoleEntity;
import com.entity.UserEntity;
import com.entity.UserRoleEntity;
import com.hibernate.HibernateMySQLUtil;
import com.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    
    @Autowired
    private RoleServiceImpl roleServiceImpl;

	@Override 
	public UserEntity getById(Long id) {
		Session	ses 		=	null;
		UserEntity	urun	=	null;
		try {
				ses = HibernateMySQLUtil.openSession();			
				urun =  (UserEntity)ses.get(UserEntity.class, id);
			
		} catch (Exception e) {
			urun.getErrorMessages().add(e.getMessage());
			HibernateMySQLUtil.rollBack(ses);
			e.printStackTrace();
		} finally {
			HibernateMySQLUtil.close(ses);
		}
        return urun;
	}
	
    @Override
    public UserEntity findUserByUserName(String userName) 	 {
    	UserEntity result = new UserEntity();
		try {
			result = HibernateMySQLUtil.loadEntityCriteria(UserEntity.class, new Object[] { "userName", userName});				
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			e.printStackTrace();
		} finally {
		}
    	return result;
    }
    
	@Override 
	public UserEntity saveUser(UserDto userDto) {	
		Session	ses 	=	null;
        UserEntity user = new UserEntity();
		try {
			ses = HibernateMySQLUtil.openSession();
			
			ses.beginTransaction();			

	        user.setUserName(userDto.getUserName());
	        user.setFirstName(userDto.getFirstName());
	        user.setLastName(userDto.getLastName());
	        user.setEmail(userDto.getEmail());
	        user.setPassword(userDto.getPassword());
	        ses.persist(user);
	        ses.flush();
	        
	        //DEFAULT ADMIN ROLU EKLENECEK
	        RoleEntity	role = roleServiceImpl.findRolByName(RoleEntity.ROLE_COLUMN_NAME_NAME);
	        if (role==null) {
	        	role = new RoleEntity();
	        	role.setId(1L);
	        	role.setName(RoleEntity.ROLE_NAME_ADMIN);	
	        }
	        UserRoleEntity userRole = new UserRoleEntity();
	        userRole.setUserId(user.getId());
	        userRole.setRole(role);
	        ses.persist(userRole);
	        ses.flush();

	        user.setRoles(Arrays.asList(userRole));			
			
			ses.getTransaction().commit();
		} catch (Exception e) {
			ses.getTransaction().rollback();
			e.printStackTrace();
		} finally{
			HibernateMySQLUtil.close(ses);
		}
		return user;
	}

    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(UserEntity user){
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}