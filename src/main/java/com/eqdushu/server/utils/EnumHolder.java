package com.eqdushu.server.utils;

public class EnumHolder {
	/**
	 * 用户类型 管理员|admin 借阅人|reader 
	 * @author lzphoenix
	 */
	public static enum UserType{
		admin, reader, guest
	}
	
	/**
	 * 用户状态
	 * @author lzphoenix
	 */
	public static enum UserStatus{
		audit, normal, abnormal
	}
	
	/**
	 * 获取场景
	 * @author lzphoenix
	 */
	public static enum SceneType{
		name, email, phone
	}

}
