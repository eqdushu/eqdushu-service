package com.eqdushu.server.utils;

public interface Constants {

    String KEY_RESPONSE_CODE = "rspCd";

    String KEY_RESPONSE_MESSAGE = "rspInf";

    String KEY_ERROR_CODE = "errorCode";

    String KEY_SUCCESS = "success";

    String LOGIN_UNKNOWN_ACCOUNT = "unknown_account";

    String LOGIN_INCORRECT_CREDENTIALS = "incorrect_credentials";

    String LOGIN_EXCESSIVE_ATTEMPTS = "excessive_attempts";

    String LOGIN_LOCKED_ACCOUNT = "locked_account";

    String LOGIN_UNKNOWN_ERROR = "unknown_error";

    String HEADER_TOKEN = "x-auth-token";

    int TOKEN_EXPIRES_HOUR = 24 * 30;

    String ENCRYPT_KEY = "e1q2d3u4s5h6u7";
    
    String TERMINAL_TYPE_ANDROID = "ANDROID";
    
    String TERMINAL_TYPE_IOS = "IOS";

    int PASSWORD_LEN_MIN = 5;
    
    int PASSWORD_LEN_MAX = 50;
    
    String LOGIN_RETRY_PRE = "login.retry:";
    
    int maxRetryCount=5;
    
    int lockTime = 3600;

}
