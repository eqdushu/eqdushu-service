package com.eqdushu.server.domain.sms;

/**
 * Created by nobita on 17/10/22.
 */
public class LMobileRep {
    private String state;

    private String msgId;

    private String msgState;

    private String reServer;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    public String getReServer() {
        return reServer;
    }

    public void setReServer(String reServer) {
        this.reServer = reServer;
    }
}
