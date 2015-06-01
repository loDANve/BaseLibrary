package com.wtm.library.utils;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import android.util.Base64;

/**
 * �ַ�������߰�
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * �жϸ��ַ��Ƿ�հ״� �հ״���ָ�ɿո��Ʊ��س����з���ɵ��ַ� �������ַ�Ϊnull����ַ�����true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * �ж��ǲ���һ���Ϸ��ĵ����ʼ���ַ
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * �ж��ǲ���һ���Ϸ����ֻ����
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * �ж�һ���ַ��ǲ�������
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * base64ת��
     *
     * @param str
     * @param chatSet
     * @return
     */
    public static String enCodeBase64(String str, String chatSet) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            return Base64.encodeToString(str.getBytes(chatSet), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("charset error", e);
        }
    }

    /**
     * base64����
     *
     * @param str
     * @param chatSet
     * @return
     */
    public static String deCodeBase64(String str, String chatSet) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            return new String(Base64.decode(str.getBytes(chatSet),
                    Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("charset error", e);
        }
    }

    /**
     * ȡMD5ժҪ
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(StringUtils.class.getName(), e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * �򵥼����㷨
     */
    public static String easyEncrypt(CharSequence str) {
        char[] cstr = str.toString().toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c + 5));
        }
        return hex.toString();
    }

    /**
     * �򵥽����㷨
     */
    public static String easyDecipher(CharSequence str) {
        char[] cstr = str.toString().toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c - 5));
        }
        return hex.toString();
    }

    /**
     * byte[] ת��Ϊ ʮ������ַ�
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");

        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * �ַ�ת����
     *
     * @param str
     * @param defValue ת��ʧ��Ĭ��ֵ
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * ����ת��
     *
     * @param obj
     * @return ת���쳣���� 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * Stringתlong
     *
     * @param obj
     * @return ת���쳣���� 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Stringתdouble
     *
     * @param obj
     * @return ת���쳣���� 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    public static double toDouble(String obj, int accurate) {
        int pointIndex = -1;
        if (obj.contains(".")) {
            pointIndex = obj.indexOf(".");
            if (obj.length() > pointIndex + accurate) {
                String tempStr = obj.substring(0, pointIndex + accurate);
                return Double.parseDouble(tempStr);
            }
        } else {
            obj += ".";
            for (int i = 0; i < accurate; i++) {
                obj += "0";
            }
        }
        return Double.parseDouble(obj);
    }

    public static String toDoubleStr(String obj, int accurate) {
        int pointIndex = -1;
        if (obj.contains(".")) {
            pointIndex = obj.indexOf(".");
            if (obj.length() > pointIndex + accurate + 1) {
                String tempStr = obj.substring(0, pointIndex + accurate + 1);
                return tempStr;
            } else {
                for (int i = 0; i < pointIndex + accurate + 1 - obj.length(); i++) {
                    obj += "0";
                }
            }
        } else {
            obj += ".";
            for (int i = 0; i < accurate; i++) {
                obj += "0";
            }
        }
        return obj;
    }

    /**
     * �ַ�ת����
     *
     * @param b
     * @return ת���쳣���� false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

}
