package com.mingchao.snsspider.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class EntityUtils2 {
	public static String toString(final HttpEntity entity) throws IOException,
			ParseException {
		return toString(entity, (Charset) null);
	}

	public static String toString(final HttpEntity entity,
			final String defaultCharset) throws IOException, ParseException {
		return toString(entity,
				defaultCharset != null ? Charset.forName(defaultCharset) : null);
	}

	public static String toString(final HttpEntity entity,
			final Charset defaultCharset) throws IOException, ParseException {
		Args.notNull(entity, "Entity");
		final InputStream instream = entity.getContent();
		if (instream == null) {
			return null;
		}
		try {
			Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
					"HTTP entity too large to be buffered in memory");
			int contentLength = (int) entity.getContentLength();
			if (contentLength < 0) {
				contentLength = 4096;
			}
			Charset charset = null;
			try {
				final ContentType contentType = ContentType.get(entity);
				if (contentType != null) {
					charset = contentType.getCharset();
				}
			} catch (final UnsupportedCharsetException ex) {
				if (defaultCharset == null) {
					throw new UnsupportedEncodingException(ex.getMessage());
				}
			}
			if (charset == null) {
				charset = defaultCharset;
			}
			int s = 0;
			int step;
			byte[] b = new byte[contentLength];
			BufferedInputStream bis = new BufferedInputStream(instream);
			while(s <contentLength && (step = bis.read(b,s,contentLength)) != -1){
				s += step;
			}
			if (charset == null) {
				Document doc = Jsoup.parse(new String(b));
				Element element = doc.select("meta[charset]").first();
				if(element !=null){
					charset = Charset.forName(element.attr("charset"));
				}else{
					element = doc.select("meta[http-equiv=content-type]").select("meta[content]").first();
					if(element != null ){
						String elestr = element.attr("content");
						Pattern pattern = Pattern.compile("charset\\s*=\\s*[^;\\s]+",Pattern.CASE_INSENSITIVE);
						Matcher m = pattern.matcher(elestr);
						if (m.find()) {
							String charsetStr =  m.group().split("\\s*=\\s*")[1].trim().toUpperCase();
							charset = Charset.forName(charsetStr);
						}
					}
				}
			}
			if (charset == null) {
				charset = StandardCharsets.UTF_8;
			}
			return new String(b,charset);
		} finally {
			instream.close();
		}
	}
}
