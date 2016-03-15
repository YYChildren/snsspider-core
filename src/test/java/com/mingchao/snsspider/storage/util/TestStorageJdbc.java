package com.mingchao.snsspider.storage.util;

import org.junit.Assert;
import org.junit.Test;

import com.mingchao.snsspider.model.UserKey;

public class TestStorageJdbc {
	@Test
	public void test(){
		UserKey uk = new UserKey();
		Assert.assertTrue(SQLUtil.getTableName(uk).equals("t_user_key"));
		Assert.assertTrue(SQLUtil.getInsertIgnoreSql(uk) == null);
		Assert.assertTrue(SQLUtil.getInsertDuplicateSql(uk) == null);
		uk.setId(1L);
		Assert.assertTrue(SQLUtil.getInsertIgnoreSql(uk).equals("INSERT IGNORE INTO t_user_key(id) VALUES(1)"));
		Assert.assertTrue(SQLUtil.getInsertDuplicateSql(uk).equals("INSERT INTO t_user_key(id) VALUES(1) ON DUPLICATE KEY UPDATE id=VALUES(id)"));
		uk.setQq(2463328396L);
		Assert.assertTrue(SQLUtil.getInsertIgnoreSql(uk).equals("INSERT IGNORE INTO t_user_key(id, qq) VALUES(1, 2463328396)"));
		Assert.assertTrue(SQLUtil.getInsertDuplicateSql(uk).equals("INSERT INTO t_user_key(id, qq) VALUES(1, 2463328396) ON DUPLICATE KEY UPDATE id=VALUES(id), qq=VALUES(qq)"));
		uk.setVisitable(true);
		System.out.println(SQLUtil.getInsertIgnoreSql(uk));
		System.out.println(SQLUtil.getInsertDuplicateSql(uk));
		uk.setDesc("userKey");
		System.out.println(SQLUtil.getInsertIgnoreSql(uk));
		System.out.println(SQLUtil.getInsertDuplicateSql(uk));
	}
}
