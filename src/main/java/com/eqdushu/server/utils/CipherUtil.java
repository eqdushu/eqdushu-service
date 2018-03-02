package com.eqdushu.server.utils;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * 加密解密编码工具类  AES、MD5、Base64
 * @author wuxy@ieyecloud.com
 * @time   2016年5月31日 下午3:33:05
 */
public class CipherUtil {
	/** 
     * AES加密 
     * @param data 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
	private static final Logger LOG = LoggerFactory
			.getLogger(CipherUtil.class);
	
    public static byte[] AESEncryptToBytes(String data, String encryptKey) throws Exception {  
    	if(data == null){
    		return null;
    	}
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128, new SecureRandom(encryptKey.getBytes()));  
  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
          
        return cipher.doFinal(data.getBytes("utf-8"));  
    }  
    
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
    public static String AESDecryptFromBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128, new SecureRandom(decryptKey.getBytes()));  
          
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
          
        return new String(decryptBytes);  
    }  
    
    
    /** 
     * AES加密为base 64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    public static String AESEncryptToBase64(String content, String encryptKey) throws Exception {  
        return base64Encode(AESEncryptToBytes(content, encryptKey));  
    }
    
    
    /** 
     * 将base 64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    public static String AESDecryptFromBase64(String encryptStr, String decryptKey) throws Exception {  
        return Strings.isNullOrEmpty(encryptStr) ? null : AESDecryptFromBytes(base64Decode(encryptStr), decryptKey);  
    }  
    
    /** 
     * 获取byte[]的md5值 
     * @param bytes byte[] 
     * @return md5 
     * @throws Exception 
     */  
    public static byte[] md5(byte[] bytes) throws Exception {  
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(bytes);  
          
        return md.digest();  
    }  
    
    /** 
     * 获取字符串md5值 
     * @param data  
     * @return md5 
     * @throws Exception 
     */  
    public static byte[] md5(String data) throws Exception {  
        return Strings.isNullOrEmpty(data) ? null : md5(data.getBytes("UTF-8"));  
    }  
    
    /** 
     * 转换为hex字符串
     */  
    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }
    
    /** 
     * 返回MD5加密字符串
     */
    public static String hash(String s) {
        try {
            return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
        	LOG.error("not supported charset...{}", e);
            return s;
        }
    }
    
    /** 
     * 结合base64实现md5加密 
     * @param data 待加密字符串 
     * @return 获取md5后转为base64 
     * @throws Exception 
     */  
    public static String md5EncryptToBase64(String data) throws Exception{  
        return Strings.isNullOrEmpty(data) ? null : base64Encode(md5(data));  
    }  
    
    
    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    public static String base64Encode(byte[] bytes){  
        return Base64.getEncoder().encodeToString(bytes);  
    }  
    
    /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return Strings.isNullOrEmpty(base64Code) ? null : Base64.getDecoder().decode(base64Code); 
    }  
    
    public static void main(String[] args) throws Exception{
//    	Long userId = 10L;
//    	String encrypt = AESEncryptToBase64(userId+"", Constants.ENCRYPT_KEY);
//    	System.out.println(encrypt);
//    	String decrypt = AESDecryptFromBase64(encrypt, Constants.ENCRYPT_KEY);
//    	System.out.println(decrypt);
    	System.out.println(hash("test"));
    }
    
    private static final byte[] SALT = "jshlsc99".getBytes(Charset.forName("UTF-8"));
    
    private static final int ITER_COUNT = 9;
    
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    
    public static String encryptAndBase64Encode(String data, String key) throws GeneralSecurityException{
    	byte[] encryptData = encrypt(data.getBytes(), key.toCharArray());
    	return base64Encode(encryptData);
    }
    
    public static String base64DecodeAndDecrypt(String data, String key) throws Exception{
    	byte[] deCodeData = base64Decode(data);
    	byte[] decryptData = decrypt(deCodeData, key.toCharArray());
    	return StringUtils.toEncodedString(decryptData, Charset.forName("utf-8"));
    }
    
    public static byte[] encrypt(byte[] data, char[] key) throws GeneralSecurityException{
        try{
            Cipher eCipher = createCiphper(key, Cipher.ENCRYPT_MODE);
            return eCipher.doFinal(data);
        }catch(GeneralSecurityException ex){
            throw ex;
        }
    }
    
    public static byte[] decrypt(byte[] data, char[] key) throws GeneralSecurityException{
        try{
            Cipher dCipher = createCiphper(key, Cipher.DECRYPT_MODE);
            return dCipher.doFinal(data);
        }catch(GeneralSecurityException ex){
            throw ex;
        }
    }
    
    private static Cipher createCiphper(char[] key, int mode) throws GeneralSecurityException{
        PBEKeySpec keySpec = new PBEKeySpec(key, SALT, ITER_COUNT);
        SecretKey secretKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITER_COUNT);
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(mode, secretKey, paramSpec);
        return cipher;
    }
    
    public static String decrypt4XCX(byte[] sessionKey,byte[] iv,byte[] encryptedData) throws Exception{
    	Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    	SecretKeySpec keyspec = new SecretKeySpec(sessionKey, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] tmp = cipher.doFinal(encryptedData);
        int unpad = tmp[tmp.length-1] & 0xFF;
        String result=new String(Arrays.copyOfRange(tmp, 0, tmp.length-unpad));
        LOG.info("Wchat xcx AES decrypt result: "+result);
        return result;
    }

}
