package com.eqdushu.server.service.sms;

import com.eqdushu.server.domain.sms.LMobileRep;
import com.eqdushu.server.domain.sms.Sms;
import com.eqdushu.server.vo.Response;

/**
 * Created by nobita on 17/10/22.
 */
public interface ISmsService {

    public Response sendSms(Sms sms);

    public boolean checkSms(String mblNo,String smsCode);
}
