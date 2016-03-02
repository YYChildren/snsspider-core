package com.mingchao.snsspider.storage.spi.jdbc;

import org.hibernate.Session;

public interface HibernateSql {
	Object execute(Session session) throws Exception;
}
