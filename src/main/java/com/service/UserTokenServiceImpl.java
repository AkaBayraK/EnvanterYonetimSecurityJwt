package com.service;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.entity.UserTokenEntity;
import com.hibernate.HibernateMySQLUtil;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    
	@Override 
	public UserTokenEntity getById(Long id) {
		Session	ses 		=	null;
		UserTokenEntity	urun	=	null;
		try {
				ses = HibernateMySQLUtil.openSession();			
				urun =  (UserTokenEntity)ses.get(UserTokenEntity.class, id);
			
		} catch (Exception e) {
			urun.getErrorMessages().add(e.getMessage());
			HibernateMySQLUtil.rollBack(ses);
			e.printStackTrace();
		} finally {
			HibernateMySQLUtil.close(ses);
		}
        return urun;
	}
	
	public UserTokenEntity save(UserTokenEntity ent) {	
		Session	ses 	=	null;
		try {
			ses = HibernateMySQLUtil.openSession();
			
			ses.beginTransaction();
			
			ses.persist(ent);
			ses.evict(ent);
			
			ses.getTransaction().commit();
		} catch (Exception e) {
			ent.getErrorMessages().add(e.getMessage());
			ses.getTransaction().rollback();
			e.printStackTrace();
		} finally{
			HibernateMySQLUtil.close(ses);
		}
		return ent;
	}

}