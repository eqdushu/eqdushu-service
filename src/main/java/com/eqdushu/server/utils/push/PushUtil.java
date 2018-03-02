package com.eqdushu.server.utils.push;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.eqdushu.server.utils.jackson.JsonUtil;
import com.google.common.base.Strings;

/**
 * 推送工具类，采用极光推送
 * 
 * @author lzphoenix@ieyecloud.com
 * @time 2017年11月19日 上午10:31:59
 */
public class PushUtil {
	private static final Logger LOG = LoggerFactory.getLogger(PushUtil.class);

	private static Properties properties;

	private static String eqdushuAppKey;
	private static String eqdushuAppSecret;
	private static String apnsEnv;
	
	private static JPushClient jpushEqdushuClient;

	static void init(Properties props) {
		properties = props;
		eqdushuAppKey = properties.getProperty("jiguang.eqdushu.appkey");
		eqdushuAppSecret = properties.getProperty("jiguang.eqdushu.appsecret");
		
		apnsEnv = properties.getProperty("jiguang.apns.env");
        //System.out.println("eqdushuAppKey: "+eqdushuAppKey +"#" +"eqdushuAppSecret"+eqdushuAppSecret);
		jpushEqdushuClient = new JPushClient(eqdushuAppSecret, eqdushuAppKey);
	}

	/**
	 * 基于注册ID推送消息
	 * 
	 * @param platform
	 * @param appType
	 * @param toSendUsers
	 * @param alert
	 * @param androidTitle
	 * @param extras
	 */
	public static void main(String args[]){
		eqdushuAppKey="7fcf508f4c4656d88a071044";
		eqdushuAppSecret="48cae1160fd8ca21ca31c4ad";
		apnsEnv="test";
		jpushEqdushuClient = new JPushClient(eqdushuAppSecret, eqdushuAppKey);
		
		String alert = "您有消息需查看";
		String androidTitle = "新消息提醒";

		String deviceIds = "140fe1da9eae6d81160";
		String[] tmp = deviceIds.split(",");
		Set<String> toSendUsers = new HashSet<String>();
		for(String deviceId: tmp ){
			System.out.println(deviceId);
			toSendUsers.add(deviceId);
		}
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("infoId", String.valueOf(2L));
		extras.put("infoExtContent", String.valueOf("152"));
		extras.put("infoType", PushUtil.MessageType.BORROW_BOOK.toString());
		extras.put("infoSts", "N");
		extras.put("infoRegion", "mlxx");// 目邻消息

		PushUtil.pushNotificationWithRegIds(PushUtil.MobilePlatform.ALL, toSendUsers, alert, androidTitle, extras);
	} 
	
	//非通知栏弹出、非apns推送
	public static void pushWithRegIds(MobilePlatform platform, Set<String> toSendUsers, String alert,
			String androidTitle, Map<String, String> extras) {
		
		if(CollectionUtils.isEmpty(toSendUsers)){
			LOG.warn("toSendUsers is empty. toSendUsers: " + toSendUsers);
			return;
		}
		toSendUsers = toSendUsers.stream().filter(u -> u != null).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(toSendUsers)){
			LOG.warn("toSendUsers is empty after filter. toSendUsers: " + toSendUsers);
			return;
		}
		PushPayload messagePayload = buildMessagePushObject(platform, null, toSendUsers, alert, androidTitle, extras);

		try {
			PushResult result = jpushEqdushuClient.sendPush(messagePayload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			LOG.error("Should review the error, and fix the request. HTTP Status: " + e.getStatus() + ", Error Code: "
					+ e.getErrorCode() + ", Error Msg: " + e.getMessage(), e);
		}
	}

	/**
	 * 广播推送消息， 推送安装应用的所有设备。 慎用！！！
	 * 
	 * @param platform
	 * @param appType
	 * @param alert
	 * @param androidTitle
	 * @param extras
	 */
	public static void pushBroadcast(MobilePlatform platform, String alert, String androidTitle,
			Map<String, String> extras) {

		PushPayload messagePayload = buildMessagePushObject(platform, null, null, alert, androidTitle, extras);

		try {
			PushResult result = jpushEqdushuClient.sendPush(messagePayload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			LOG.error("Should review the error, and fix the request. HTTP Status: " + e.getStatus() + ", Error Code: "
					+ e.getErrorCode() + ", Error Msg: " + e.getMessage(), e);
		}
	}

	/**
	 * 基于注册ID推送通知
	 * 
	 * @param platform
	 * @param appType
	 * @param toSendUsers
	 * @param alert
	 * @param androidTitle
	 * @param extras
	 */
	public static void pushNotificationWithRegIds(MobilePlatform platform, Set<String> toSendUsers,
			String alert, String androidTitle, Map<String, String> extras) {
		
		if(CollectionUtils.isEmpty(toSendUsers)){
			LOG.warn("toSendUsers is empty. toSendUsers: " + toSendUsers);
			return;
		}
		toSendUsers = toSendUsers.stream().filter(u -> u != null).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(toSendUsers)){
			LOG.warn("toSendUsers is empty after filter. toSendUsers: " + toSendUsers);
			return;
		}
		
		PushPayload messagePayload = buildNotificationPushObject(platform, null, toSendUsers, alert, androidTitle,
				extras);
		try {
			PushResult result = jpushEqdushuClient.sendPush(messagePayload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			LOG.error("Should review the error, and fix the request. HTTP Status: " + e.getStatus() + ", Error Code: "
					+ e.getErrorCode() + ", Error Msg: " + e.getMessage(), e);
		}
	}

	/**
	 * 广播推送通知， 推送安装应用的所有设备。 慎用！！！
	 * 
	 * @param platform
	 * @param appType
	 * @param alert
	 * @param androidTitle
	 * @param extras
	 */
	public static void pushNotificationBroadcast(MobilePlatform platform, String alert,
			String androidTitle, Map<String, String> extras) {

		PushPayload messagePayload = buildNotificationPushObject(platform, null, null, alert, androidTitle, extras);
		try {
			PushResult result = jpushEqdushuClient.sendPush(messagePayload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			LOG.error("Should review the error, and fix the request. HTTP Status: " + e.getStatus() + ", Error Code: "
					+ e.getErrorCode() + ", Error Msg: " + e.getMessage(), e);
		}
	}

	/**
	 * 构建通知对象，如果根据标签分组推送， 则设置tag，如果根据注册ID推送，则设置regIds，如果两者都为空，则推送所有设备
	 * 
	 * @param platform
	 * @param tag
	 * @param regIds
	 * @param alert
	 * @param androidTitle
	 * @param extras
	 * @return
	 */
	private static PushPayload buildNotificationPushObject(MobilePlatform platform, String tag, Set<String> regIds,
			String alert, String androidTitle, Map<String, String> extras) {
		PushPayload.Builder builder = PushPayload.newBuilder();
		switch (platform) {
		case ALL:
			builder.setPlatform(Platform.all());
			break;
		case Android:
			builder.setPlatform(Platform.android());
			break;
		case IOS:
			builder.setPlatform(Platform.ios());
			break;
		default:
			builder.setPlatform(Platform.all());
			break;
		}
		if (!Strings.isNullOrEmpty(tag)) {
			builder.setAudience(Audience.tag(tag));
		} else if (CollectionUtils.isNotEmpty(regIds)) {
			builder.setAudience(Audience.registrationId(regIds));
		} else {
			builder.setAudience(Audience.all());
		}

		extras.put("infoTitle", androidTitle);
		extras.put("infoContent", alert);
		
		boolean apns = true;
		if("test".equals(apnsEnv)){
			apns =false;
		}
		
		PushPayload payload = builder
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert)
								// .setTitle(androidTitle)
								.addExtras(extras).build())
						.addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(extras).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(apns).build()).build();

		return payload;
	}

	/**
	 * 构建消息对象，如果根据标签分组推送， 则设置tag，如果根据注册ID推送，则设置regIds，如果两者都为空，则推送所有设备
	 * 
	 * @param platform
	 * @param tag
	 * @param regIds
	 * @param alert
	 * @param androidTitle
	 * @param extras
	 * @return
	 */
	private static PushPayload buildMessagePushObject(MobilePlatform platform, String tag, Set<String> regIds,
			String alert, String androidTitle, Map<String, String> extras) {
		PushPayload.Builder builder = PushPayload.newBuilder();
		switch (platform) {
		case ALL:
			builder.setPlatform(Platform.all());
			break;
		case Android:
			builder.setPlatform(Platform.android());
			break;
		case IOS:
			builder.setPlatform(Platform.ios());
			break;
		default:
			builder.setPlatform(Platform.all());
			break;
		}
		if (!Strings.isNullOrEmpty(tag)) {
			builder.setAudience(Audience.tag(tag));
		} else if (CollectionUtils.isNotEmpty(regIds)) {
			builder.setAudience(Audience.registrationId(regIds));
		} else {
			builder.setAudience(Audience.all());
		}

		extras.put("infoTitle", androidTitle);
		extras.put("infoContent", alert);
		PushPayload payload = builder.setMessage(Message.newBuilder().setMsgContent(JsonUtil.toJson(extras)).build())
				.build();

		return payload;
	}

	public static enum MobilePlatform {
		ALL, Android, IOS;
	}

	public static enum MessageType {
		BORROW_BOOK, // 借书提醒
		NEED_RERUEN_BOOK, // 即将还书提醒
		RERUEN_BOOK//已还书提醒
	}

}
