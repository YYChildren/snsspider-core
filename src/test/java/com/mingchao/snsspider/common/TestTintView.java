package com.mingchao.snsspider.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.mingchao.snsspider.login.LoginParam;
import com.mingchao.snsspider.login.view.LoginParamView;

public class TestTintView {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new TestTintView().test();
	}
	
	public void test(){
		try {
			LoginParam dialog = new LoginParamView();
			dialog.setTint("user", "用户名");
			dialog.setTint("password", "密码", true);
			try {
				dialog.setTint("captcha", "验证码",
						Files.readAllBytes(Paths.get("D:\\captcha.gif")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			dialog.submit();
			System.out.println(dialog.getLoginInfo());
			System.out.println(true);
			Thread.sleep(1000);
			dialog.retry();
			System.out.println(dialog.getLoginInfo());
			System.out.println(true);
			dialog.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
