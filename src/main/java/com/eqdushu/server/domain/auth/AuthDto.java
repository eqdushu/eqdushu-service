package com.eqdushu.server.domain.auth;

import com.eqdushu.server.domain.BaseDto;

public class AuthDto extends BaseDto {

	private static final long serialVersionUID = 56211231380L;
	
	private String userType;//用户类型
	private Long userId;
	private String uuid; //uuid
	private String xAuthToken;//登录token
	private String phone;
	private String deviceId; // 手机设备ID，用于消息推送
	private Long companyId;//公司ID，不存在则为空
	private String company;//公司名称，不存在则为空
	
	public String getUserType() {
		return userType;
	}
	public Long getUserId() {
		return userId;
	}
	public String getUuid() {
		return uuid;
	}
	public String getxAuthToken() {
		return xAuthToken;
	}
	public String getPhone() {
		return phone;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public void setxAuthToken(String xAuthToken) {
		this.xAuthToken = xAuthToken;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public void setCompany(String company) {
		this.company = company;
	}

}
