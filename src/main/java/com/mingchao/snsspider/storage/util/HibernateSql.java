package com.mingchao.snsspider.storage.util;

import org.hibernate.Session;

public interface HibernateSql {
	Object execute(Session session) throws Exception;
}
