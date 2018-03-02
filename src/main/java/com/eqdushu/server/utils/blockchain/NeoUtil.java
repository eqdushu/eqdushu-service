package com.eqdushu.server.utils.blockchain;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eqdushu.server.utils.http.HttpUtil;
import com.eqdushu.server.utils.jackson.JsonUtil;
import com.google.common.base.Strings;


public class NeoUtil {
	private static final Logger LOG = LoggerFactory.getLogger(NeoUtil.class);
	
	private static Properties properties;

	private static String neoUrl;
	public static String neoAsset;
	public static String neoMainAddress;
	
	static void init(Properties props) {
		properties = props;
		neoUrl = properties.getProperty("neo.wallet.url");
		neoAsset = properties.getProperty("neo.asset.name");
		neoMainAddress = properties.getProperty("neo.main.address");
	}
	
	/** 
	 * 从指定地址，向指定地址转账
	 * http://docs.neo.org/zh-cn/node/api/sendfrom.html
	 * from：转账地址
	 * to: 收款地址
	 * value：转账金额
	 * fee：手续费，可选参数，默认为 0
	 * */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendFrom(String[] params) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jsonrpc", "2.0");
		paramMap.put("method", "sendfrom");
		paramMap.put("params", params);
		paramMap.put("id", 1);
		String json = JsonUtil.toJson(paramMap);
		Map<String, String> headerMap = new HashMap<String, String>();
		String result = HttpUtil.doJsonPost(neoUrl, json, headerMap);
		System.out.println(result);
		if (Strings.isNullOrEmpty(result)) {
			LOG.error("fail to get send from.");
			return null;
		}
		return JsonUtil.fromJson(result, Map.class);
	}
	
	/** 
	 * 创建一个新的地址
	 * */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getNewAddress() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jsonrpc", "2.0");
		paramMap.put("method", "getnewaddress");
		paramMap.put("params", new String[0]);
		paramMap.put("id", 1);
		String json = JsonUtil.toJson(paramMap);
		Map<String, String> headerMap = new HashMap<String, String>();
		String result = HttpUtil.doJsonPost(neoUrl, json, headerMap);
		System.out.println(result);
		if (Strings.isNullOrEmpty(result)) {
			LOG.error("fail to get new address.");
			return null;
		}
		return JsonUtil.fromJson(result, Map.class);
	}

	
	/** 
	 * 根据指定的资产编号，返回钱包中对应资产的余额信息
	 * http://docs.neo.org/zh-cn/node/api/getbalance.html 
	 * balance：钱包中该资产的真实余额
	 * confirmed：钱包中该资产的已确认的金额，只有已确认的金额可以用来转账
	 * balance 和 confirmed 二者可能会不相等，仅在从钱包中转出一笔钱，而且有找零未确认时时，confirmed 值会小于balance。当这笔交易确定后，二者会变得相等
	 * */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getBalance(String[] params) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jsonrpc", "2.0");
		paramMap.put("method", "getbalance");
		paramMap.put("params", params);
		paramMap.put("id", 1);
		String json = JsonUtil.toJson(paramMap);
		Map<String, String> headerMap = new HashMap<String, String>();
		String result = HttpUtil.doJsonPost(neoUrl, json, headerMap);
		System.out.println(result);
		if (Strings.isNullOrEmpty(result)) {
			LOG.error("fail to get balance.");
			return null;
		}
		return JsonUtil.fromJson(result, Map.class);
	}

	
	// 查询账户地址资产
	public static NeoReturn getAccountState(String[] params) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jsonrpc", "2.0");
		paramMap.put("method", "getaccountstate");
		paramMap.put("params", params);
		paramMap.put("id", 1);
		String json = JsonUtil.toJson(paramMap);
		Map<String, String> headerMap = new HashMap<String, String>();
		String result = HttpUtil.doJsonPost(neoUrl, json, headerMap);
		System.out.println(result);
		if (Strings.isNullOrEmpty(result)) {
			LOG.error("fail to get account state.");
			return null;
		}
		return JsonUtil.fromJson(result, NeoReturn.class);
	}

	/** 
	 * 获取区块高度
	 * */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getBlockCount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jsonrpc", "2.0");
		paramMap.put("method", "getblockcount");
		paramMap.put("params", new String[0]);
		paramMap.put("id", 1);
		String json = JsonUtil.toJson(paramMap);
		Map<String, String> headerMap = new HashMap<String, String>();
		String result = HttpUtil.doJsonPost(neoUrl, json, headerMap);
		System.out.println(result);
		if (Strings.isNullOrEmpty(result)) {
			LOG.error("fail to get block count.");
			return null;
		}
		return JsonUtil.fromJson(result, Map.class);
	}
	
	

	public static void main(String[] args) {
		/*String[] params = new String[1];
		params[0] = "AV9zVh3X6D79QotXg1SsUWef5tXdiMsWjV";
		NeoReturn r = NeoUtil.getAccountState(params);
		System.out.print(r.getJsonrpc());*/
		//BigDecimal b = new BigDecimal(Math.random()/10);
		Double neoNum = 0.06;
		String[] params_ = new String[4];
		params_[0] = NeoUtil.neoAsset;
		params_[1] = NeoUtil.neoMainAddress;
		params_[2] = "AYcgCuTLBbCabKXwfAyTzkEcDpR5VuKdsz";
		params_[3] = String.valueOf(neoNum);
		NeoUtil.sendFrom(params_);
	}

}
