package com.eqdushu.server.system;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppConfiguration {

	private AppConfiguration() {}

	private static Map<String, String> appPropertiesMap;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static void init(Properties properties) {
		appPropertiesMap = Collections.unmodifiableMap(new HashMap(properties));
	}

	public static Map<String, String> getAppPropertiesMap() {
		return appPropertiesMap;
	}

	public static String getValue(String key){
		return appPropertiesMap.get(key);
	}

}
