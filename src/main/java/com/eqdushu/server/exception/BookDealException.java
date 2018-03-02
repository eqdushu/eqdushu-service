package com.eqdushu.server.exception;

import com.eqdushu.server.utils.ResponseCode;

/**
 * Created by nobita on 17/9/18.
 */
public class BookDealException extends Exception{


    private ResponseCode code;

    public BookDealException(ResponseCode code){
        super();
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }
}
