package com.iflytek.web.util;

import java.util.UUID;

public class UuidUtils {
	public static String getUuid32() {
		return UUID.randomUUID().toString().replace("-", "");        
	}
	public static String getUuid36() {
		return UUID.randomUUID().toString();
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}
