package com.huangxj.common.core.utils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @className: HexToDataUtils
 * @description: TODO
 * @author: hjx
 * @create: 2019-05-16
 **/
public class HexToDataUtils {

    /**
     * 根据ascii将字节数组逐个转换，按顺序合并为String
     * @param byteArrays
     * @return
     */
    public static String asciiToString(byte[] byteArrays) {
        if(byteArrays.length < 1 || byteArrays == null){
            return null;
        }
        int len = byteArrays.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) byteArrays[i];
        }
        return new String(chars);
    }

    /**
     * 小端模式将字节数组转为int（最大支持4个字节）
     * @param byteArrays
     * @return
     */
    public static int getInt(byte[] byteArrays){
        if(byteArrays.length < 1 || byteArrays == null){
            return 0;
        }
        int length = byteArrays.length;
        switch (length) {
            case 1:
                return (int) byteArrays[0];
            case 2:
                return (byteArrays[0] & 0xff) | (byteArrays[1] << 8 & 0xff00);
            case 3:
                return (byteArrays[0] & 0xff) | (byteArrays[1] << 8 & 0xff00) | (byteArrays[2] << 16 & 0xff0000);
            case 4:
                return (byteArrays[0] & 0xff) | (byteArrays[1] << 8 & 0xff00) | (byteArrays[2] << 16 & 0xff0000)
                        | (byteArrays[3] << 24 & 0xff000000);
            default:
                return 0;
        }
    }

    /**
     * 小端模式将字节数组转为ip字符串（最大支持4个字节）
     * @param byteArrays
     * @return
     */
    public static String getIp(byte[] byteArrays){
        if(byteArrays.length < 1 || byteArrays == null){
            return null;
        }
        int ip = HexToDataUtils.getInt(byteArrays);

        StringBuffer sb = new StringBuffer("");
        // 将高24位置0
        sb.append((ip & 0x000000FF));
        sb.append(".");

        // 将高16位置0，然后右移8位
        sb.append((ip & 0x0000FFFF) >>> 8);
        sb.append(".");

        // 将高8位置0，然后右移16位
        sb.append((ip & 0x00FFFFFF) >>> 16);
        sb.append(".");

        // 直接右移24位
        sb.append((ip >>> 24));

        return sb.toString();
    }

    public static String getHexIp(byte[] byteArrays){
        if(byteArrays.length < 1 || byteArrays == null){
            return null;
        }

        StringBuffer sb = new StringBuffer("");
        // 将高24位置0
        sb.append(getInt(Arrays.copyOfRange(byteArrays, 0, 1)));
        sb.append(".");

        // 将高16位置0，然后右移8位
        sb.append(getInt(Arrays.copyOfRange(byteArrays, 1, 2)));
        sb.append(".");

        // 将高8位置0，然后右移16位
        sb.append(getInt(Arrays.copyOfRange(byteArrays, 2, 3)));
        sb.append(".");
        // 直接右移24位
        sb.append(getInt(Arrays.copyOfRange(byteArrays, 3, 4)));
        return sb.toString();
    }

    /**
     * 小端模式将字节数组转为mac字符串（最大支持4个字节）
     * @param byteArrays
     * @return
     */
    public static String getMac(byte[] byteArrays){
        if(byteArrays.length < 1 || byteArrays == null){
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArrays.length; i++) {
            if(i > 0){
                sb.append(":");
            }
            sb.append(getID(Arrays.copyOfRange(byteArrays, i, i+1)));
        }

        return sb.toString();
    }

    /**
     * 将byte数组转化为16进制字符串
     * @param byteArrays
     * @return
     */
    public static String getID(byte[] byteArrays){
        if(byteArrays.length < 1 || byteArrays == null){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (byteArrays == null || byteArrays.length <= 0) {
            return null;
        }
        for (int i = 0; i < byteArrays.length; i++) {
            int v = byteArrays[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();

    }

    /**
     * byte数组转日期
     * @param byteArrays
     * @return
     */
    public static String getDate(byte[] byteArrays){
        if(byteArrays.length < 1 || byteArrays == null){
            return null;
        }
        int len = byteArrays.length;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            if(i==0){
                stringBuffer.append(""+(2000+(int)byteArrays[i]));
            }else {
                stringBuffer.append(((int)byteArrays[i]));
            }
            if(i != len - 1){
                stringBuffer.append("/");
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d/H/m/s");
        LocalDateTime localDateTime = LocalDateTime.parse(stringBuffer.toString(), formatter);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter1.format(localDateTime);
    }

    /**
     * 根据编码将字节流转换为中文
     * @param bytes
     * @return
     */
    public static String getChinese(byte[] bytes){
        if(bytes.length < 1 || bytes == null){
            return null;
        }
        String str = null;
        try {
            str = new String(bytes, "UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return str;
    }

    /**
     * 将字节数组转为二进制字符串
     * @param tByte
     * @return
     */
    public static String getBinary(Byte tByte){
        return Integer.toBinaryString((tByte & 0xFF) + 0x100).substring(1);
    }
}
