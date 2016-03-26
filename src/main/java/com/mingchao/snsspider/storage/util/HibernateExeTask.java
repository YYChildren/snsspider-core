package com.mingchao.snsspider.storage.util;

import org.hibernate.Session;

public interface HibernateExeTask {
	Object execute(Session session);
}
