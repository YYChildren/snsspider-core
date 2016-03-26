package com.mingchao.snsspider.storage.util;

import org.hibernate.cfg.Configuration;

public class HibernateUtilJdbc extends HibernateUtil{
	@SuppressWarnings("deprecation")
	public HibernateUtilJdbc(String resource) {
		try {
			sessionFactory = new Configuration().configure(resource).buildSessionFactory();
		} catch (Throwable ex) {
			log.error("Initial SessionFactory creation failed." , ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
