package com.eqdushu.server.exception;

public class PasswdException extends RuntimeException {

	private static final long serialVersionUID = 56783822113456L;

    public enum Code {
    	passwd_check_failed,//修改密码时原密码不正确
    	account_notexsit//修改密码时用户账号不存在
    }
    
    private final Code code; 
    
    private int retryLimitCount;
    
    private int retryCount;
    
    private int lockTimeInSeconds;
    
    public PasswdException(Code code){
        super();
        this.code = code;
    }

    public PasswdException(Code code, int retryLimitCount, int retryCount,
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
