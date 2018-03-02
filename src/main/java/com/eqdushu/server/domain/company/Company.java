package com.eqdushu.server.domain.company;

import java.util.Objects;

import com.eqdushu.server.domain.BaseDomain;

public class Company extends BaseDomain {
	
	private static final long serialVersionUID = 3657679018271L;
	
    private String name;
	
	private String address;
	
	private String provinceId;
	
	private String cityId;
	
	private String remark;
	
	private String code;//公司管理员预设唯一编码
	
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public String getRemark() {
		return remark;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)return true;
        if(!(obj instanceof Company))return false;
        Company other = (Company)obj;
        return Objects.equals(id, other.id);
	}

}
