package com.eqdushu.server.domain.user;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;

import com.eqdushu.server.domain.BaseDomain;
import com.eqdushu.server.utils.EnumHolder.UserStatus;
import com.eqdushu.server.utils.EnumHolder.UserType;

public class User extends BaseDomain {

	private static final long serialVersionUID = 3456777882331L;
	
	private Long companyId;
	
	private String company;
	
	private String phone;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String salt;//用于MD5加密
	
	private UserType userType;
	
	private String termType;
	
	private String channel;//注册渠道
	
	private UserStatus status;//注册用户状态
	
	private Boolean deleted;//是不是做逻辑删除
	
	private String userName;//用户姓名

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getSalt() {
		return salt;
	}

	public UserType getUserType() {
		return userType;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public String getCompany() {
		return company;
	}

	public String getTermType() {
		return termType;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setTermType(String termType) {
		this.termType = termType;
	}
	
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
     * 生成新的种子
     */
    public void randomSalt() {
        setSalt(RandomStringUtils.randomAlphanumeric(10));
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)return true;
        if(!(obj instanceof User))return false;
        User other = (User)obj;
        return Objects.equals(id, other.id);
	}

}
