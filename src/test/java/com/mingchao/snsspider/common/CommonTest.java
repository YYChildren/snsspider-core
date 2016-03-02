package com.mingchao.snsspider.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

public class CommonTest {
	@Test
	public void testURLEncodedUtils(){
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("para","{a=b}"));
		System.out.println(URLEncodedUtils.format(parameters,HTTP.DEF_CONTENT_CHARSET ));
	}
}
