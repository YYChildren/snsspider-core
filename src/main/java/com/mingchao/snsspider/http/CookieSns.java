package com.mingchao.snsspider.http;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.openqa.selenium.Cookie;

public class CookieSns extends Cookie implements SetCookie, ClientCookie,
		Cloneable, Serializable {

	private static final long serialVersionUID = -3491059098092431400L;

	private String snsname;
	private String snsvalue;
	private String snspath;
	private String snsdomain;
	private Date snsexpiry;
	private boolean snssecure;
	private boolean snshttpOnly;
	private boolean snshostOnly;
	private boolean snssession;
	private String snscomment;
	private int snsversion = 0;


	public CookieSns(org.apache.http.cookie.Cookie cookie) {
		this(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie
				.getPath(), cookie.getExpiryDate());
	}
	
	public CookieSns(Cookie cookie) {
		this(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie
				.getPath(), cookie.getExpiry());
	}

	public CookieSns(CookieSns cookie) {
		this(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie
				.getPath(), cookie.getExpiry(), cookie.isSecure(), cookie
				.isHttpOnly(), cookie.isSession(), cookie.isHostonly());
	}

	public CookieSns(String name, String value) {
		this(name, value, "/", null);
	}

	public CookieSns(String name, String value, String path) {
		this(name, value, path, null);
	}

	public CookieSns(String name, String value, String path, Date expiry) {
		this(name, value, null, path, expiry);
	}

	public CookieSns(String name, String value, String domain, String path,
			Date expiry) {
		this(name, value, domain, path, expiry, false);
	}

	public CookieSns(String name, String value, String domain, String path,
			Date expiry, boolean isSecure) {
		this(name, value, domain, path, expiry, isSecure, false);
	}

	public CookieSns(String name, String value, String domain, String path,
			Date expiry, boolean isSecure, boolean isHttpOnly) {
		this(name, value, domain, path, expiry, isSecure, isHttpOnly, true);
	}

	public CookieSns(String name, String value, String domain, String path,
			Date expiry, boolean isSecure, boolean isHttpOnly, boolean session) {
		this(name, value, domain, path, expiry, isSecure, isHttpOnly, session,
				true);
	}

	public CookieSns(String name, String value, String domain, String path,
			Date expiry, boolean isSecure, boolean isHttpOnly, boolean session,
			boolean hostOnly) {
		// 仅是为了满足父类的构造函数，无意义
		super(name, value, domain, path, expiry, isSecure, isHttpOnly);

		this.snsname = name;
		this.snsvalue = value;
		this.snspath = path == null || "".equals(path) ? "/" : path;
		this.snsdomain = (domain == null) ? null : domain.split(":")[0];
		this.snssecure = isSecure;
		this.snshttpOnly = isHttpOnly;

		if (expiry != null) {
			// Expiration date is specified in seconds since (UTC) epoch time,
			// so truncate the date.
			this.snsexpiry = new Date(expiry.getTime() / 1000 * 1000);
		} else {
			this.snsexpiry = null;
		}
		this.snssession = session;
		this.snshostOnly = hostOnly;
	}

	@Override
	public void setValue(String value) {
		this.snsvalue = value;
	}

	@Override
	public void setComment(String comment) {
		this.snscomment = comment;
	}

	@Override
	public void setExpiryDate(Date expiryDate) {
		this.snsexpiry = expiryDate;
	}

	@Override
	public void setDomain(String domain) {
		this.snsdomain = domain;
	}

	@Override
	public void setPath(String path) {
		this.snspath = path;
	}

	@Override
	public void setSecure(boolean secure) {
		this.snssecure = secure;
	}

	@Override
	public void setVersion(int version) {
		this.snsversion = version;
	}

	@Override
	public String getName() {
		return this.snsname;
	}

	@Override
	public String getValue() {
		return this.snsvalue;
	}

	@Override
	public String getDomain() {
		return this.snsdomain;
	}

	@Override
	public String getPath() {
		return this.snspath;
	}

	@Override
	public boolean isSecure() {
		return this.snssecure;
	}

	@Override
	public boolean isHttpOnly() {
		return this.snshttpOnly;
	}

	@Override
	public Date getExpiry() {
		return this.snsexpiry;
	}

	@Override
	public String getComment() {
		return this.snscomment;
	}

	@Override
	public String getCommentURL() {
		return null;
	}

	@Override
	public Date getExpiryDate() {
		return getExpiry();
	}

	@Override
	public boolean isPersistent() {
		return (null != getExpiry());
	}

	@Override
	public int[] getPorts() {
		return null;
	}

	@Override
	public int getVersion() {
		return this.snsversion;
	}

	@Override
	public boolean isExpired(Date date) {
		Args.notNull(date, "Date");
		return (getExpiry() != null && getExpiry().getTime() <= date.getTime());
	}

	public boolean isHostonly() {
		return this.snshostOnly;
	}

	public boolean isSession() {
		return this.snssession;
	}

	@Override
	public String getAttribute(String name) {
		try {
			return String.valueOf(getField(name).get(this));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean containsAttribute(String name) {
		return getField(name) != null;
	}

	private Field getField(String name) {
		try {
			Field field = this.getClass().getDeclaredField("sns" + name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}

	@Override
	public void validate() {
		if (snsname == null || "".equals(snsname) || snsvalue == null
				|| snspath == null) {
			throw new IllegalArgumentException(
					"Required attributes are not set or "
							+ "any non-null attribute set to null");
		}

		if (snsname.indexOf(';') != -1) {
			throw new IllegalArgumentException(
					"Cookie names cannot contain a ';': " + snsname);
		}

		if (snsdomain != null && snsdomain.contains(":")) {
			throw new IllegalArgumentException(
					"Domain should not contain a port: " + snsdomain);
		}
	}

	@Override
	public String toString() {
		return snsname
				+ "="
				+ snsvalue
				+ (snsexpiry == null ? "" : "; expires="
						+ new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z")
								.format(snsexpiry))
				+ ("".equals(snspath) ? "" : "; path=" + snspath)
				+ (snsdomain == null ? "" : "; domain=" + snsdomain)
				+ (snssecure ? ";secure;" : "");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CookieSns)) {
			return false;
		}

		CookieSns cookie = (CookieSns) o;

		if (!snsname.equals(cookie.snsname)) {
			return false;
		}
		return !(snsvalue != null ? !snsvalue.equals(cookie.snsvalue)
				: cookie.snsvalue != null);
	}

	@Override
	public int hashCode() {
		return snsname.hashCode();
	}
}
