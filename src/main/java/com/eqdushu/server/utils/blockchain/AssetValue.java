package com.eqdushu.server.utils.blockchain;

import java.io.Serializable;

public class AssetValue implements Serializable{
	
	private static final long serialVersionUID = 5738213095L;
	private String asset;
    private String value;
    
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    
}
