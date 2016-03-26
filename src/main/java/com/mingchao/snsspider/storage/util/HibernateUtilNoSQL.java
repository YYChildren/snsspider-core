package com.mingchao.snsspider.storage.util;

import org.hibernate.ogm.cfg.OgmConfiguration;

public class HibernateUtilNoSQL extends HibernateUtil{
	@SuppressWarnings("deprecation")
	public HibernateUtilNoSQL(String resource) {
		try {
			sessionFactory = new OgmConfiguration().configure(resource).buildSessionFactory();
		} catch (Throwable ex) {
			log.error("Initial SessionFactory creation failed." , ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
