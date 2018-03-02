package com.eqdushu.server.utils.blockchain;

import java.io.Serializable;

public class NeoReturn implements Serializable {

	private static final long serialVersionUID = 567489391L;
	private String jsonrpc;
	private NeoResult result;
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public NeoResult getResult() {
		return result;
	}
	public void setResult(NeoResult result) {
		this.result = result;
	}
	

}
