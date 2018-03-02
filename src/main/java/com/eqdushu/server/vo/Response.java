package com.eqdushu.server.vo;

import com.eqdushu.server.utils.ResponseCode;

import java.util.Date;

public class Response extends BaseVo{
	
	private static final long serialVersionUID = 7891536433869957038L;
	
	private String rspCd;
	private String rspInf;
	private Long rspTm;
	private Object data;
	
	public Response(){}
	
	public Response(ResponseCode code){
		this.rspCd = code.name();
		this.rspInf = code.getMsg();
		this.rspTm = new Date().getTime();
	}
	
	public Response(ResponseCode code, Object data){
		this.rspCd = code.name();
		this.rspInf = code.getMsg();
		this.rspTm = new Date().getTime();
		this.data = data;
	}
	
	public Response(ResponseCode code, Long rspTm){
		this.rspCd = code.name();
		this.rspInf = code.getMsg();
		this.rspTm = rspTm;
	}
	
	public Response(String code,String msg){
		this.rspCd = code;
		this.rspInf = msg;
		this.rspTm = new Date().getTime();
	}
	
	public Response(String code,String msg,Object data){
		this.rspCd = code;
		this.rspInf = msg;
		this.rspTm = new Date().getTime();
		this.data = data;
	}
	
	public String getRspCd() {
		return rspCd;
	}
	public void setRspCd(String rspCd) {
		this.rspCd = rspCd;
	}
	public String getRspInf() {
		return rspInf;
	}
	public void setRspInf(String rspInf) {
		this.rspInf = rspInf;
	}
	public Long getRspTm() {
		return rspTm;
	}
	public void setRspTm(Long rspTm) {
		this.rspTm = rspTm;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
