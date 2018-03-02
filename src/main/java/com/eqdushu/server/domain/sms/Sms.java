package com.eqdushu.server.domain.sms;

import com.eqdushu.server.domain.BaseDomain;

/**
 * Created by nobita on 17/10/14.
 */
public class Sms extends BaseDomain{

	private static final long serialVersionUID = 83892920031L;

	private String mblNo;

    private String typ;//注册下发短信 R  找回密码下发短信 F

    private LMobileRep lmRep;

    public String getMblNo() {
        return mblNo;
    }

    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public LMobileRep getLmRep() {
        return lmRep;
    }

    public void setLmRep(LMobileRep lmRep) {
        this.lmRep = lmRep;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
