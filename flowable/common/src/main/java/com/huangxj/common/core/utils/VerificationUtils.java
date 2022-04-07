package com.huangxj.common.core.utils;

/**
 * @className: CRCUtils
 * @description: TODO
 * @author: hjx
 * @create: 2019-05-17
 **/
public class VerificationUtils {

    private static int crcCode = 0x1021;


    public static byte[] crc(byte[] bytes){

        int crc = 0;

        for(int n = 0; n < bytes.length; n++){
            for(short i = 0x80;i != 0; i >>= 1){
                if((crc & 0x8000) != 0){
                    crc <<= 1;
                    crc ^= crcCode;
                } else {
                    crc <<= 1;
                }

                if((bytes[n] & i) != 0){
                    crc ^= crcCode;
                }
            }
        }

        byte[] result = new byte[2];
        //由高位到低位
        result[0] = (byte)(crc & 0xFF);
        result[1] = (byte)((crc >> 8) & 0xFF);

        return result;
    }

    public static byte xor(byte[] bytes){
        byte temp = 0;
        for (byte aByte : bytes) {
            temp ^= aByte;
        }

        return temp;
    }
}
