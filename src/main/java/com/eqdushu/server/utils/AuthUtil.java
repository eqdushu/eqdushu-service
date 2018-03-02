package com.eqdushu.server.utils;


public class AuthUtil {
	
	private static final ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

	public static void setUserId(Long userId){
		threadLocal.set(userId);
	}
	
	public static Long getUserId(){
		return threadLocal.get();
	}
	
	public static void clear(){
		threadLocal.remove();
	}
}
