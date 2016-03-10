package com.mingchao.snsspider.util;

import java.security.InvalidParameterException;

public class Numeric {
	public final static byte[]  toBytes(long number){
		int byteSize = Long.SIZE / Byte.SIZE;
		byte[] bytes = new byte[byteSize];
		for (int i = 0; i < byteSize; i++) {
			bytes[i] = (byte) (number >>> (byteSize - i - 1) & 0xFF);
		}
		return bytes;
	}
	
	public final static byte[]  toBytes(int number){
		int byteSize = Integer.SIZE / Byte.SIZE;
		byte[] bytes = new byte[byteSize];
		for (int i = 0; i < byteSize; i++) {
			bytes[i] = (byte) (number >>> (byteSize - i - 1) & 0xFF);
		}
		return bytes;
	}
	
	public final static byte[] toBytes(String hexStr) {
		char[] hexCharArray = hexStr.toCharArray();
		byte[] bytes = new byte[hexCharArray.length / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] |= toByte(hexCharArray[2 * i]) << 4 ;
			bytes[i] |= toByte(hexCharArray[2 * i + 1]);
		}
		return bytes;
	}

	public final static byte toByte(char c) {
		if (c >= '0' && c <= '9') {
			return (byte) (c - '0');
		} else if (c >= 'A' && c <= 'F') {
			return (byte) (c - 'A' + 10);
		} else if (c >= 'a' && c <= 'f') {
			return (byte) (c - 'a' + 10);
		} else {
			throw new InvalidParameterException("Not a hex char: "+ c);
		}
	}
}
