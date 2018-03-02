package com.eqdushu.server.exception;

public class SignInException extends RuntimeException {

	private static final long serialVersionUID = 56783822113456L;

    public enum Code {
        unkonw_account,  //未知的帐户
        audit_account,//图书管理员账号审核中
        abnormal_account, //帐户状态异常
        incorrect_credentials,//错误的授权登录信息
        unknow_error //未知的错误
    }
    
    private final Code code; 
    
    private int retryLimitCount;
    
    private int retryCount;
    
    private int lockTimeInSeconds;
    
    public SignInException(Code code){
        super();
        this.code = code;
    }

    public SignInException(Code code, int retryLimitCount, int retryCount,
            int lockTimeInSeconds) {
        super();
        this.code = code;
        this.retryLimitCount = retryLimitCount;
        this.retryCount = retryCount;
        this.lockTimeInSeconds = lockTimeInSeconds;
    }

    public Code getCode() {
        return code;
    }

    public int getRetryLimitCount() {
        return retryLimitCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public int getLockTimeInSeconds() {
        return lockTimeInSeconds;
    }

}
