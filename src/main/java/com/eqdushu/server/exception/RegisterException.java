package com.eqdushu.server.exception;

public class RegisterException extends RuntimeException {
	private static final long serialVersionUID = 23571872289636L;

    public static enum Code{
    	empty_account,//账号为空
        invalid_account,//无有效账号
        account_exsited,//账号已存在
        empty_company//管理员注册时公司为空
    }

    private Code code;

    public RegisterException(Code code){
        super();
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
