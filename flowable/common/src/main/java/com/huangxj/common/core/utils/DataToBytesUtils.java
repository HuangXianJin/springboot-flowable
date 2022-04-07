package com.huangxj.common.core.utils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @className: DataToBytesUtils
 * @description: TODO
 * @author: hjx
 * @create: 2019-05-28
 **/
public class DataToBytesUtils {

    /**
     * 数字转byte数组
     * @param num
     * @param len
     * @return
     */
    public static byte[] integerToBytes(Integer num, Integer len){
        if(len < 1){
            return null;
        }
        byte[] bytes = new byte[len];
        switch (len) {
            case 1: bytes[0] = (byte)( num & 0xff);
                return bytes;
            case 2: bytes[0] = (byte)( num  & 0xff);
                bytes[1] = (byte)( num >> 8 & 0xff);
                return bytes;
            case 3:bytes[0] = (byte)( num  & 0xff);
                bytes[1] = (byte)( num >> 8 & 0xff);
                bytes[2] = (byte)( num >> 16 & 0xff);
                return bytes;
            case 4:bytes[0] = (byte)( num  & 0xff);
                bytes[1] = (byte)( num >> 8 & 0xff);
                bytes[2] = (byte)( num >> 16 & 0xff);
                bytes[3] = (byte)( num >> 24 & 0xff);
                return bytes;
            default:
                return bytes;
        }
    }

    /**
     * 字符串转byte数组
     * @param str
     * @return
     */
    public static byte[] stringToBytes(String str, Integer len){
        if(str.length() < 1){
            return null;
        }
        char[] chars = str.toCharArray();
        byte[] bytes = new byte[len];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    /**
     * 中文字符串转byte数组
     * @param str
     * @return
     */
    public static byte[] chineseToBytes(String str, Integer len){
        if(str.length() < 1 || Objects.equals(str, null)){
            return null;
        }
        byte[] bytes = new byte[len];
        Charset charset = Charset.forName("UTF-16LE");
        byte[] strs = str.getBytes(charset);
        for (int i = 0; i < strs.length; i++) {
            bytes[i] = strs[i];
        }
        return bytes;
    }

    /**
     * 小端模式将ip字符串转为字节数组（最大支持4个字节）
     * @param ip
     * @return
     */
    public static byte[] ipToBytes(String ip, Integer len){
        if(ip.length() < 1 || Objects.equals(ip, null)){
            return null;
        }
        byte[] bytes = new byte[len];
        String[] ips = ip.split("\\.");

        for (int i = 0; i < ips.length; i++) {
            int temp = Integer.parseInt(ips[i]);
            bytes[i] = (byte) temp;
        }

        return bytes;
    }

    /**
     * 小端模式将mac字符串转为字节数组（最大支持4个字节）
     * @param mac
     * @return
     */
    public static byte[] macToBytes(String mac, Integer len){
        if(mac.length() < 1 || Objects.equals(mac, null)){
            return null;
        }
        String[] macs = mac.split(":");
        byte[] bytes = new byte[len];

        for (int i = 0; i < macs.length; i++) {
            byte[] temp = idToBytes(macs[i], macs.length);
            bytes[i] = temp[0];
        }

        return bytes;
    }

    /**
     * 将byte数组转化为16进制字符串
     * @param hexString
     * @return
     */
    public static byte[] idToBytes(String hexString, Integer len){
        if(hexString.length() < 1 || Objects.equals(hexString, null)){
            return null;
        }
        String hex = "0123456789ABCDEF";
        hexString = hexString.toUpperCase();
        int dataLen = hexString.length() / 2;

        byte[] bytes = new byte[len];

        int pos;
        int temp;
        for (int i = 0; i < dataLen; i++) {
            pos = i * 2;
            temp = Integer.decode("0x" + hexString.substring(pos, pos+1) + hexString.substring(pos+1, pos+2));
            bytes[i] = (byte)temp;
        }
        return bytes;
    }

    /**
     * 日期转bytes
     * @param date
     * @return
     */
    public static byte[] dateToBytes(Date date, Integer len){
        if(date == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/M/d/H/m/s");
        LocalDateTime localDateTime = date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();

        String dateStr = formatter.format(localDateTime);

        String[] strs = dateStr.split("/");

        byte[] bytes = new byte[len];

        for (int i = 0; i < 6; i++) {
            int temp = Integer.parseInt(strs[i]);
            bytes[i] = (byte) temp;
        }

        return bytes;
    }

    /**
     * 将一个字节的二进制字符串转为byte数组
     * @param binary
     * @return
     */
    public static byte[] binaryToBytes(String binary){
        byte[] bytes = new byte[1];
        int temp = Integer.parseInt(binary, 2);
        bytes[0] = (byte) temp;
        return bytes;
    }

}
