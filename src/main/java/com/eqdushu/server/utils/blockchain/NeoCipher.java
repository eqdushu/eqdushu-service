package com.eqdushu.server.utils.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NeoCipher {
	/**
	 * 利用java原生的摘要实现SHA256加密
	 * 
	 * @param 需加密的str
	 * @return 加密后的byte
	 */
	public static byte[] getSHA256Byte(String str) {
		MessageDigest messageDigest;
		byte[] resut = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			resut = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resut;
	}

	/**
	 * 利用java原生的摘要实现SHA256加密
	 * 
	 * @param 需加密的str
	 * @return 加密后的str
	 */
	public static String getSHA256Str(String str) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}

	/**
	 * 将byte转为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	public static String computeHash(String input) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		byte[] utf8bytes = null;
		try {
			utf8bytes = input.getBytes("UTF-8");
			// digest.update(utf8bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteData = digest.digest(utf8bytes);
		//return byteData;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String address = "AJShjraX4iMJjwVt8WYYzZyGvDMxw6Xfbe";
		//System.out.println(computeHash(address));
		//byte[] tmp = getSHA256Byte(address);
		//byte[] result = encodeRipeMD160(tmp);
		//System.out.println(byte2Hex(result));

		//System.out.println(getSHA256Str(address));
		//System.out.println(RipeMDCoder.encodeRipeMD160Hex(tmp));
	}

}
