package com.eqdushu.server.service.sms;

import com.eqdushu.server.domain.sms.LMobileRep;
import com.eqdushu.server.domain.sms.Sms;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.SmsUtil;
import com.eqdushu.server.utils.date.DateTimeUtil;
import com.eqdushu.server.vo.Response;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by nobita on 17/10/22.
 */
@Service
public class SmsServiceImpl implements  ISmsService{

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Response sendSms(Sms sms) {
        String mblNo = sms.getMblNo();
        String smsRedisKey = "msg:" + mblNo;
        LMobileRep lmRep = new LMobileRep();
        //判断是否存在
        Object object = (String) redisTemplate.boundValueOps(smsRedisKey).get();
        if(null!=object){
            //60秒可重新下发  300秒才失效
            Map map = (Map) object;
            Date oDate = (Date) map.get("curTm");
            if(DateTimeUtil.compareDate(new Date(),oDate,60)) {
                return new Response(ResponseCode.smserror_frequent);
            }
        }
        //生成四位随机数
        String smsCode = new Random().nextInt(999999)+"";
        try {
            lmRep = SmsUtil.sendSms(sms,smsCode);
            String state = lmRep.getState();

            if("0".equals(state)){
                Map map = new HashMap();
                map.put("smsCode",smsCode);
                map.put("curTm", new Date());
                redisTemplate.boundValueOps(smsRedisKey).set(
                        map, 300, TimeUnit.SECONDS);
            }else{
                return new Response(ResponseCode.sendsms_error);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new Response(ResponseCode.sendsms_error);
        }
        return new Response(ResponseCode.success);
    }

    @Override
    public boolean checkSms(String mblNo,String smsCode) {
        String smsRedisKey = "msg:" + mblNo;
        //判断是否存在
        Object obj = (String) redisTemplate.boundValueOps(smsRedisKey).get();
        if(null==obj){
            return false;
        }
        if(obj instanceof Map){
            Map map = (Map) obj;
            String oSmsCode = (String) map.get("smsCode");
            if(!smsCode.equals(oSmsCode)){
                return false;
            }
        }else{
            String oSmsCode = (String) obj;
            if(!smsCode.equals(oSmsCode)){
                return false;
            }
        }

        return true;
    }
}
