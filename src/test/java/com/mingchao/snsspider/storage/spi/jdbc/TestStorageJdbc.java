package com.mingchao.snsspider.storage.spi.jdbc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mingchao.snsspider.model.UserKey;

public class TestStorageJdbc {

	StorageMySQL sms;

	@Before
	public void setUp() throws Exception {
		sms = new StorageMySQL();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test(){
		UserKey uk = new UserKey();
		Assert.assertTrue(sms.getTableName(uk).equals("`t_user_key`"));
		Assert.assertTrue(sms.getInsertIgnoreSql(uk) == null);
		Assert.assertTrue(sms.getInsertDuplicateSql(uk) == null);
		uk.setId(1L);
		Assert.assertTrue(sms.getInsertIgnoreSql(uk).equals("INSERT IGNORE INTO `t_user_key`(`id`) VALUES(1)"));
		Assert.assertTrue(sms.getInsertDuplicateSql(uk).equals("INSERT INTO `t_user_key`(`id`) VALUES(1) ON DUPLICATE KEY UPDATE `id`=VALUES(`id`)"));
		uk.setQq(2463328396L);
		Assert.assertTrue(sms.getInsertIgnoreSql(uk).equals("INSERT IGNORE INTO `t_user_key`(`id`, `qq`) VALUES(1, 2463328396)"));
		Assert.assertTrue(sms.getInsertDuplicateSql(uk).equals("INSERT INTO `t_user_key`(`id`, `qq`) VALUES(1, 2463328396) ON DUPLICATE KEY UPDATE `id`=VALUES(`id`), `qq`=VALUES(`qq`)"));
		uk.setVisitable(true);
		System.out.println(sms.getInsertIgnoreSql(uk));
		System.out.println(sms.getInsertDuplicateSql(uk));
		uk.setDesc("userKey");
		System.out.println(sms.getInsertIgnoreSql(uk));
		System.out.println(sms.getInsertDuplicateSql(uk));
		
	}
	
	
}
