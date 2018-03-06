package com.eqdushu.server.utils;

import com.eqdushu.server.domain.sms.LMobileRep;
import com.eqdushu.server.domain.sms.Sms;
import com.eqdushu.server.mapper.sms.SmsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by nobita on 17/10/14.
 */
public class SmsUtil {


    public static LMobileRep sendSms(Sms sms,String smsCode) throws UnsupportedEncodingException {
        String typ = sms.getTyp();
        String mblNo = sms.getMblNo();
        String content = "";
        if("R".equals(typ)){
            content = "您的验证码是："+smsCode+"，1分钟有效【EQ读书】";
        }else if("F".equals(typ)){
            content = "您的验证码是："+smsCode+"，1分钟有效【EQ读书】";
        }else{
            LMobileRep rep = new LMobileRep();
            rep.setMsgState("9999");
            rep.setMsgState("下发短信请求类型错误");
            return rep;
        }
        String PostData = "sname=dlzhanghua0&spwd=zh112233&scorpid=&sprdid=1012888&sdst="+sms.getMblNo()+"&smsg="+ URLEncoder.encode(content,"utf-8");
        String result = SMS(PostData, "http://cf.51welink.com/submitdata/Service.asmx/g_Submit");
        LMobileRep rep = XMLParseUtil.extract(result);
        return rep;
    }

    public static String SMS(String postData, String postUrl) {
        try {
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }
    public static void main(String[] args)throws Exception{

        String PostData = "sname=dlzhanghua0&spwd=zh112233&scorpid=&sprdid=1012888&sdst=13574856074&smsg="+ URLEncoder.encode("您的验证码是：658218，5分钟有效【EQ读书】","utf-8");
//        String PostData = "sname=dlzhanghua&spwd=zh111111&scorpid=&sprdid=1112888&sdst=13574856074&smsg="+ URLEncoder.encode("您的验证码是：658218，5分钟有效【EQ读书】","utf-8");

        //out.println(PostData);
        String ret = SMS(PostData, "http://cf.51welink.com/submitdata/Service.asmx/g_Submit");
        System.out.println(ret);

        LMobileRep lMobileRep = XMLParseUtil.extract(ret);
        System.out.println(lMobileRep.getMsgState());
    }
}
