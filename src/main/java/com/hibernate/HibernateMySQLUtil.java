package com.hibernate;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entity.UserEntity;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class HibernateMySQLUtil {
	
	
	public static void main(String[] args) {
		Session	ses	=	null;
		try {
//			ses	=	HibernateMySQLUtil.openSession();
//			
//			
//			ses.beginTransaction();

			System.out.println("_BASE64_:" + Decoders.BASE64.decode("kafeinkafeinkafeinkafeinkafeinkafeinkafeinkafeinkafein"));
			System.out.println("_KEY_:" + Keys.hmacShaKeyFor(Decoders.BASE64.decode("kafeinkafeinkafeinkafeinkafeinkafeinkafeinkafeinkafein")));
			
//			UserEntity usr = ses.get(UserEntity.class, 1L);
//			System.out.println("_USER_:" + (usr==null?"":usr.getUserName()));
			
			/*
			List<EnvanterEntity>	result = null;			
			BigDecimal tplKalanUrunAdeti = BigDecimal.ZERO;
//			List<EnvanterEntity>	result = HibernateMySQLUtil.loadEntityListByNamedQuery(EnvanterEntity.class, EnvanterEntity.FIND_ENVANTER_URUN_ALL,new Long[] {1L});		
			
		
	        CriteriaBuilder cb = ses.getCriteriaBuilder();
	        CriteriaQuery<EnvanterEntity> cq = cb.createQuery(EnvanterEntity.class);
	        Root<EnvanterEntity> kural = cq.from(EnvanterEntity.class);
	        List<Predicate> criteria = new ArrayList<Predicate>();
			criteria.add(cb.equal(kural.get("urunId"), 1L));
	        cq.select(kural).where(criteria.toArray(new Predicate[]{}));
	        result = ses.createQuery(cq).getResultList();
	        ses.close();
	        
			BigDecimal	girenUrunAdedi = BigDecimal.ZERO;
			BigDecimal	cikanUrunAdedi = BigDecimal.ZERO;
			try {
				girenUrunAdedi = result.stream().filter(x->x.getGirisTrh()!=null).map(EnvanterEntity::getAdet).reduce((a,b)->a.add(b)).get();
			} catch (Exception e) {
				girenUrunAdedi = BigDecimal.ZERO;
			}
			try {
				cikanUrunAdedi = result.stream().filter(x->x.getCikisTrh()!=null).map(EnvanterEntity::getAdet).reduce((a,b)->a.add(b)).get();
			} catch (Exception e) {
				cikanUrunAdedi = BigDecimal.ZERO;
			}
			tplKalanUrunAdeti = (girenUrunAdedi==null?BigDecimal.ZERO:girenUrunAdedi).add(cikanUrunAdedi==null?BigDecimal.ZERO:cikanUrunAdedi);
			
			
			//Urun ı sine göre envanterdeki tüm listesi çek ve girş tarihi ürünün giren adet sayisi , cikis tarihi olanlar ise ürünün cikan adet sayisi , aradaki farkta kalanı versin.
			//java 8 deki sortu kullan
			System.out.println(" Üründe kalan toplam adet sayısı : "+tplKalanUrunAdeti);
			if (tplKalanUrunAdeti.compareTo(new BigDecimal(10))==-1) {
				System.out.println(" Üründe kalan toplam adet sayısı : "+tplKalanUrunAdeti);
			}
			
*/
			
//			ses.merge();
//			ses.evict();
			
//			ses.getTransaction().commit();
			
		} catch (Throwable e) {
//			ses.getTransaction().rollback();
			HibernateMySQLUtil.rollBack(ses);
			e.printStackTrace();
		}finally {
			HibernateMySQLUtil.close(ses);
			System.out.println("islem tamamlandi.");
			System.exit(1);
		}
		
		
		
	}
	
	protected static Logger logger = LoggerFactory.getLogger(HibernateMySQLUtil.class);
	private final static Set<Class<?>> classes;
	private final static ArrayList<String> resources;

	static {
		classes = new HashSet<Class<?>>();
		resources = new ArrayList<String>();
		Reflections reflections = new Reflections(new ConfigurationBuilder()
		.addUrls(ClasspathHelper.forClassLoader())
		.addUrls(ClasspathHelper.forJavaClassPath())
		.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(), new ResourcesScanner()));

		Set<Class<?>> _class = reflections.getTypesAnnotatedWith(Entity.class);
		for (final Class<?> clazz : _class) {
			classes.add(clazz);
		}

	}

	private static StandardServiceRegistry registry;
	private static BufferedReader br;
	
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/envanterDB?useSSL=false");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "root");
                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "none"); /* ENTİTY deki colonlara göre tablo drop edip tekrardan create eder*/

                configuration.setProperties(settings);

                //configuration.addAnnotatedClass(Student.class);
				for (final Class<?> clazz : classes) {
					configuration.addAnnotatedClass(clazz);
				}
				 for (final String str : resources) {
		            configuration.addResource(str);
		        }

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
    
	public static Session openSession() {
		Session session	=	HibernateMySQLUtil.getSessionFactory().openSession();
		return session;
	}
	
	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
	
	public static void rollBack(Session session) {
		try {
			if (session != null) {
				if (session.isOpen()) {
					session.getTransaction().rollback();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Session session) throws HibernateException {
		try {
			if (session != null) {
				if (session.isOpen()) {
					try {
						// rollback exception olursa session close olması için
						if (session.getTransaction().isActive()) {
							session.getTransaction().rollback();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				session.close();
				session = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <T> List<T> loadAllData(Class<T> type, Session session) {
	    CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<T> criteria = builder.createQuery(type);
	    criteria.from(type);
	    List<T> data = session.createQuery(criteria).getResultList();
	    return data;
	  }

	public static  <T extends Object> T loadEntityCriteria(Class<T> entityClass, Object[] columnsValues) throws Exception{
		Session ses = null;
		T result;
		try {
		    ses = HibernateMySQLUtil.getSessionFactory().openSession();
			result = loadEntityCriteria(ses, entityClass, columnsValues);
			ses.clear();
		}catch (Exception e) {
			throw e;
		}finally {
			close(ses);
		}
		return result;
	}
	public static <T extends Object> T loadEntityCriteria(Session ses, Class<T> entityClass, Object[] columnsValues) throws Exception{
		try {
		    if(columnsValues.length % 2 != 0) {
		    	throw new RuntimeException(entityClass.getSimpleName() + " loadEntityCriteria metoduna gönderilen columnsValues dizisinin eleman sayısı ikinin katları olmalı.");
		    }
			CriteriaBuilder builder = ses.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(entityClass);
			Root<T> root = criteria.from(entityClass);
			List<Predicate> predicateList = new ArrayList<Predicate>();
			for (int i = 0; i < columnsValues.length; i++) {
				if (i % 2 == 0) {
					if(columnsValues[i + 1] != null) {
						predicateList.add(builder.equal(getFieldPath(root, (String) columnsValues[i]), columnsValues[i + 1]));
					}
				}
			}
			criteria.select(root).where(predicateList.toArray(new Predicate[] {}));
			return ses.createQuery(criteria).uniqueResult();
		}catch (Exception e) {
			throw e;
		}
	}
	
	public static <T> List<T> loadListCriteria(Class<T> entityClass, Object[] columnsValues) throws Exception{
		Session ses = null;
		List<T> result;
		try {
		    ses = HibernateMySQLUtil.getSessionFactory().openSession();
			result = loadListCriteria(ses, entityClass, columnsValues);
			ses.clear();
		}catch (Exception e) {
			throw e;
		}finally {
			close(ses);
		}
		return result;
	}
	
	public static <T> List<T> loadListCriteria(Session ses, Class<T> entityClass, Object[] columnsValues) throws Exception{
		try {
		    if(columnsValues.length % 2 != 0) {
		    	throw new RuntimeException(entityClass.getSimpleName() + " loadListCriteria metoduna gönderilen columnsValues dizisinin eleman sayısı ikinin katları olmalı.");
		    }
			CriteriaBuilder builder = ses.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(entityClass);
			Root<T> root = criteria.from(entityClass);
			List<Predicate> predicateList = new ArrayList<Predicate>();
			for (int i = 0; i < columnsValues.length; i++) {
				if (i % 2 == 0) {
					if(columnsValues[i + 1] != null) {
						predicateList.add(builder.equal(getFieldPath(root, (String) columnsValues[i]), columnsValues[i + 1]));
					}
				}
			}
			criteria.select(root).where(predicateList.toArray(new Predicate[] {}));
			return ses.createQuery(criteria).getResultList();
		}catch (Exception e) {
			throw e;
		}
	}
	
	public static Path<Object> getFieldPath(Root<?> root, String column) {
		Path<Object> path = null;
		try {
			String [] dizi = column.split("\\.");
			if(dizi.length == 1){
				path = root.get(dizi[0]);
			}else if(dizi.length == 2) {
				path = root.get(dizi[0]).get(dizi[1]);
			}else if(dizi.length == 3) {
				path = root.get(dizi[0]).get(dizi[1]).get(dizi[2]);
			}else if(dizi.length == 4) {
				path = root.get(dizi[0]).get(dizi[1]).get(dizi[2]).get(dizi[3]);
			}else {
				throw new RuntimeException("HibernateUtil.getFieldPath column adi en fazla 4 seviye olabilir.");
			}
		}catch (Exception e) {
			throw e;
		}
		return path;
	}
	
}
