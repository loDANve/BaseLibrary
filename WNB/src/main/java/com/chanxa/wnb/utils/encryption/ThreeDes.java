package com.chanxa.wnb.utils.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/*字符串 DESede(3DES) 加密*/
public class ThreeDes {
	/**
	 * @param args在java中调用sun公司提供的3DES加密解密算法时
	 *            ，需要使 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包： jce.jar
	 *            security/US_export_policy.jar security/local_policy.jar
	 *            ext/sunjce_provider.jar
	 */
	//private static final String Algorithm = "DESede"; // 定义加密算法,可用
														// DES,DESede,Blowfish
	private static final String Algorithm = "DESede/ECB/PKCS5Padding"; 

	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	public static byte[] encryptMode(byte[] keybyte, byte[] src) throws Exception{
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);// 在单一方面的加密或解密
		} catch (Exception e1) {
			throw e1;
		}
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	public static byte[] decryptMode(byte[] keybyte, byte[] src) throws Exception {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (Exception e) {
			throw e;
		}
	}

	// 转换成十六进制字符串
	public static String byte2Hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) throws Exception {

		byte[] byteKey = HexStringToBinary("A5F2D6E5684437A527708B9BDE5AB8438630554A4578CEDF");
		byte[] byteIv = HexStringToBinary("EDE6048B814904A6");
		
		
		String straa ="/vqYmVodn%2Bj02skgXWGGqTf7Joc5Add/6zVcs1qjfgwMJ1POw%2BOKM%2BW/2eyvSm7O";
		straa = straa.replace("%2B", "+");
		//byte[] data = new BASE64Decoder().decodeBuffer(straa);
		byte[] data = Base64.decode(straa, Base64.DEFAULT);
		byte[] srcBytes = decryptMode(byteKey, data);
		System.out.println("解密后的字符串:" + (new String(srcBytes)));
		// TODO Auto-generated method stub
		// 添加新安全算法,如果用JCE就要把它添加进去

		// A5 F2 D6 E5 68 44 37 A5 27 70 8B 9B DE 5A B8 43 86 30 55 4A 45 78 CE
		// DF
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		final byte[] keyBytes = { (byte) 0xA5, (byte) 0xF2, (byte) 0xD6,
//				(byte) 0xE5, (byte) 0x68, 0x44, 0x37, (byte) 0xA5, 0x27, 0x70,
//				(byte) 0x8B, (byte) 0x9B, (byte) 0xDE, (byte) 0x5A,
//				(byte) 0xB8, 0x43, (byte) 0x86, 0x30, 0x55, (byte) 0x4A, 0x45,
//				0x78, (byte) 0xCE, (byte) 0xDF }; // 24字节的密钥
//		String szSrc = "<DATA/>";
//		System.out.println("加密前的字符串:" + szSrc);
//		byte[] encoded = encryptMode(byteKey, szSrc.getBytes("UTF-8"));
//		
//		System.out.println("加密后的字符串:" +new BASE64Encoder().encode(encoded));
//		byte[] srcBytes = decryptMode(byteKey, encoded);
//		System.out.println("解密后的字符串:" + (new String(srcBytes)));
	}
	
	public static String jieMi(String key,String data) throws Exception {
		byte[] byteKey = ThreeDes.HexStringToBinary(key);
		data = data.replace("%2B", "+");
		byte[] dataArr = Base64.decode(data, Base64.DEFAULT);
		byte[] srcBytes = ThreeDes.decryptMode(byteKey, dataArr);
		return new String(srcBytes);
	}
	
	public static String jiaMi(String key,String data) throws Exception {
		byte[] byteKey = ThreeDes.HexStringToBinary(key);
		byte[] encoded = ThreeDes.encryptMode(byteKey, data.getBytes("UTF-8"));	
		String result = new String(Base64.encode(encoded, Base64.DEFAULT));
		return result;
	}
	

	private static String hexStr = "0123456789ABCDEF";

	/**
	 * 
	 * @param hexString
	 * @return 将十六进制转换为字节数组
	 */
	public static byte[] HexStringToBinary(String hexString) {
		// hexString的长度对2取整，作为bytes的长度
		int len = hexString.length() / 2;
		byte[] bytes = new byte[len];
		byte high = 0;// 字节高四位
		byte low = 0;// 字节低四位

		for (int i = 0; i < len; i++) {
			// 右移四位得到高位
			high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
			low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
			bytes[i] = (byte) (high | low);// 高地位做或运算
		}
		return bytes;
	}
}
