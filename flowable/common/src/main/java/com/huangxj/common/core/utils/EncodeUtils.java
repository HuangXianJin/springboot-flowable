package com.huangxj.common.core.utils;

import cn.hutool.core.text.UnicodeUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 *
 * @author Liuyadu
 * @version 2015-04-15
 */
public class EncodeUtils {

    private static final String ENCODING = "UTF-8";
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            return null;
        }
    }

    /**
     * Base64编码.
     */
    public static String encodeBase64(byte[] input) {
        return new String(Base64.encodeBase64(input));
    }

    /**
     * Base64编码.
     */
    public static String encodeBase64(String input) {
        try {
            return new String(Base64.encodeBase64(input.getBytes(ENCODING)));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input.getBytes());
    }

    /**
     * Base64解码.
     */
    public static String decodeBase64String(String input) {
        try {
            return new String(Base64.decodeBase64(input.getBytes()), ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Base62编码。
     */
    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String encodeUrl(String part) {
        try {
            return URLEncoder.encode(part, ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String decodeUrl(String part) {
        try {
            return URLDecoder.decode(part, ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    public static byte[] hexToBytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();

        // 如果 hex 中的字符不是偶数个, 则忽略最后一个
        byte[] bytes = new byte[hexChars.length / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }

    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    /**
     * byte数组 转换成 16进制小写字符串
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }

        return hex.toString();
    }

    public static String transEscreenCardCode(String cardUuidString) {
        //取中间8个字符数据转成16进制后为卡内码
        //两位两位倒序
        String cardCode1 = cardUuidString.substring(4, 6);
        String cardCode2 = cardUuidString.substring(6, 8);
        String cardCode3 = cardUuidString.substring(8, 10);
        String cardCode4 = cardUuidString.substring(10, 12);
        StringBuilder cardCodeStringBuilder = new StringBuilder();
        cardCodeStringBuilder.append(cardCode3).append(cardCode4).append(cardCode1).append(cardCode2);
        String cardCode = cardCodeStringBuilder.toString();

        //卡内码转10进制
        String x = new BigInteger(cardCode, 16).toString(10);

        //补0个数
        int c = 10 - x.length();
        StringBuilder cardCodeBuilder = new StringBuilder();
        for (int i = 0; i < c; i++) {
            cardCodeBuilder.append("0");
        }
        cardCodeBuilder.append(x);
        return cardCodeBuilder.toString();
    }


    /**
     * Unicode格式十六进制 字符串
     * 7A66FD80895B3296D08FF47ED176A763FB7CDF7E00"
     *
     * @param str
     * @return
     */
    public static String unicode2str(String str) {
        char[] strArr = str.toCharArray();
        StringBuilder unicode = new StringBuilder();
        int count = strArr.length / 4;
        for (int index = 0; index < count; ++index) {
            int flag = index * 4;
            unicode.append("\\u");
            unicode.append(strArr[flag + 2]);
            unicode.append(strArr[flag + 3]);
            unicode.append(strArr[flag + 0]);
            unicode.append(strArr[flag + 1]);
        }
        return UnicodeUtil.toString(unicode.toString());
    }

    //字符(串)==>Unicode编码(串)
    public static String str2unicode(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        char[] strArr = str.toCharArray();
        StringBuilder unicode = new StringBuilder();
        String temp = "";
        for (int index = 0; index < strArr.length; ++index) {
            temp = Integer.toHexString(strArr[index]).toUpperCase();
            while (temp.length() - 4 != 0) temp = "0" + temp;

            unicode.append(temp.charAt(2));
            unicode.append(temp.charAt(3));
            unicode.append(temp.charAt(0));
            unicode.append(temp.charAt(1));
        }
        return unicode.toString();
    }

    public static void main(String[] args) {

        String temp = "重合闸模块";
        String temp1 = str2unicode(temp);
        System.out.println(temp1);
        String str = unicode2str(temp1);
        System.out.println(str);
        System.out.println(unicode2str("CD910854F895B0650D54F079"));
        System.out.println(unicode2str("CD910854F895B0650D54F079310032003300310032003300"));
    }
}
