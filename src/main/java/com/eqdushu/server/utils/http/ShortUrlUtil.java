package com.eqdushu.server.utils.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.eqdushu.server.utils.jackson.JsonUtil;

public class ShortUrlUtil {

	private final static String SERVICE_API = "http://apis.baidu.com/3023/shorturl/shorten";
	private final static String API_KEY = "70d2fae52b11aa35d837490e831de399";

	@SuppressWarnings("unchecked")
	public static String shortenUrl(String longUrl) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", API_KEY);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		Map<String, String> params = new HashMap<String, String>();
		params.put("url_long", longUrl);
		String response = HttpUtil.doFormPost(SERVICE_API, params, headers);
		if (StringUtils.isNotEmpty(response)) {
			Map<String, Object> result = JsonUtil.fromJson(response, Map.class);
			if (result != null) {
				List<Map<String, Object>> urlMaps = (List<Map<String, Object>>) MapUtils
						.getObject(result, "urls");
				if (CollectionUtils.isNotEmpty(urlMaps)) {
					Map<String, Object> urlMap = urlMaps.get(0);
					return MapUtils.getString(urlMap, "url_short");
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out
				.println(shortenUrl("http://node.ieyecloud.com/osapp/yuyuewenzhen/page/step1_index?orderItemId=109&siteId=122"));
	}
}
