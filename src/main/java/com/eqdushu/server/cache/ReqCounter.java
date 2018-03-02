package com.eqdushu.server.cache;

import java.io.Serializable;

public class ReqCounter implements Serializable{

	private static final long serialVersionUID = -292778145513834833L;
	
	private int c; //访问次数
	private long t; //上次访问时间unix时间戳，单位毫秒
	
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public long getT() {
		return t;
	}
	public void setT(long t) {
		this.t = t;
	}
}
