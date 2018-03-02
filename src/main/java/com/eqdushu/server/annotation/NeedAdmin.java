package com.eqdushu.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在Controller的方法上使用此注解，表明该操作需要用户是管理员
 * @author lzphoenix
 * @time   2016年6月1日 下午3:16:48
 */
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface NeedAdmin {

}
