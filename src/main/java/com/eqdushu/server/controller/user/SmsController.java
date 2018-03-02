package com.eqdushu.server.controller.user;

import com.eqdushu.server.annotation.NonAuthorization;
import com.eqdushu.server.domain.sms.Sms;
import com.eqdushu.server.service.sms.ISmsService;
import com.eqdushu.server.vo.Response;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by nobita on 17/10/22.
 */
@Controller
@RequestMapping("/urm")
@LogInfo
@LogException(value = { @LogException.Exc(value = Exception.class, stacktrace = false) }, warn = { @LogException.Exc({ IllegalArgumentException.class }) })
public class SmsController {

    @Autowired
    private ISmsService iSmsService;
    @NonAuthorization
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    public @ResponseBody Response sendSms(
            @RequestBody Map<String, Object> params) {
        String mblNo = MapUtils.getString(params, "mblNo");
        String typ = MapUtils.getString(params, "smsTyp");
        Sms sms = new Sms();
        sms.setMblNo(mblNo);
        sms.setTyp(typ);

        Response response = iSmsService.sendSms(sms);
        return response;
    }

}
