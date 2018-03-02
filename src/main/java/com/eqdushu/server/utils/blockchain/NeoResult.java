package com.eqdushu.server.utils.blockchain;

import java.io.Serializable;
import java.util.List;

public class NeoResult implements Serializable{
	private static final long serialVersionUID = 3456732341L;
	private String version;
	private Boolean frozen;
	private List<AssetValue> balances;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Boolean getFrozen() {
		return frozen;
	}
	public void setFrozen(Boolean frozen) {
		this.frozen = frozen;
	}
	public List<AssetValue> getBalances() {
		return balances;
	}
	public void setBalances(List<AssetValue> balances) {
		this.balances = balances;
	}
	
}
