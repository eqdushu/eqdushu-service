package com.eqdushu.server.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by nobita on 17/11/19.
 */
public class VersionUtil {

    /**
     * 检查带dot的版本号
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareStringHavingDot(String str1,String str2){
        int result = 0;
        byte[] b1 = new byte[0];
        try {
            b1 = str1.getBytes("utf-8");
            byte[] b2 = str2.getBytes("utf-8");

            if(b1.length >= b2.length){
                for(int i = 0,len = b2.length; i < len; i++){
                    result |= Byte.valueOf(b1[i]).compareTo(Byte.valueOf(b2[i]));
                }
            }else if(b1.length < b2.length){
                for(int i = 0,len = b1.length; i < len; i++){
                    result |= Byte.valueOf(b1[i]).compareTo(Byte.valueOf(b2[i]));
                }
                if(result == 0){//如果前两位都相等,长度长的较大
                    result = -1;
                }
            }
            if(result >= 0){
                return true;
            }else{
                //说明需要更新
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return true;
    }
}
