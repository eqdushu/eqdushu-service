package com.eqdushu.server.domain.user;

import com.eqdushu.server.domain.BaseDomain;

/***
 * 用户登录后额外信息
 * @author lzphoenix
 * @time   2016年5月17日 上午11:22:13
 */
public class UserExtra extends BaseDomain{
	
	private static final long serialVersionUID = 1598837155720964016L;
	
	private Long userId;
	private String userName; //用户名
	private String uuid; //unique uid
	private String phone;//手机号码
	private String deviceId; // 手机设备ID，用于消息推送
	private String userType;//用户类型
	private String termType;//使用终端
	private Long companyId;//所在公司ID
	private String company;//所在公司
	private String channel;//渠道号
	
	public Long getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getUuid() {
		return uuid;
	}
	public String getPhone() {
		return phone;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public String getUserType() {
		return userType;
	}
	public String getChannel() {
		return channel;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTermType() {
		return termType;
	}
	public String getCompany() {
		return company;
	}
	public void setTermType(String termType) {
		this.termType = termType;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

}
