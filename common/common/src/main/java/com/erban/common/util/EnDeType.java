package com.erban.common.util;

/**
 * Created by pc on 2015/5/6.
 */
public class EnDeType {

    //////////////////////////////////////////////////////////////////
    private static final char HEXES[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String byteToHexString(byte[] data) {
        if (data == null) {
            return null;
        }

        final int len = data.length;
        if (0 == len)
            return "";

        char strBuf[] = new char[2 * len];
        for (int i = 0; i < len; ++i) {
            strBuf[i * 2] = HEXES[(data[i] & 0xF0) >>> 4];
            strBuf[i * 2 + 1] = HEXES[(data[i] & 0x0F)];
        }
        String result = new String(strBuf);
        return result;
    }

    public static String byteToHexString(byte[] data, int offset, int count) {
        if (data == null) {
            return null;
        }

        int remainLen = data.length - offset;
        int len = count > remainLen ? remainLen : count;
        if (0 == len)
            return "";

        char strBuf[] = new char[2 * len];
        for (int i = 0; i < len; ++i) {
            strBuf[i * 2] = HEXES[(data[i + offset] & 0xF0) >>> 4];
            strBuf[i * 2 + 1] = HEXES[(data[i + offset] & 0x0F)];
        }
        String result = new String(strBuf);
        return result;
    }


    /**
     * 32位16进制转换为16位字节
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStringtoBytes(String hexStr) {
        byte[] bytes = new byte[hexStr.length() / 2];
        copyHexStringtoBytes(hexStr, bytes, 0, bytes.length);
        return bytes;
    }

    public static byte hexChartoByte(char c) {
        byte b = 0;
        if (c >= '0' && c <= '9') {
            b = (byte) (c - '0');
        } else if (c >= 'a' && c <= 'f') {
            b = (byte) (10 + (c - 'a'));
        } else if (c >= 'A' && c <= 'F') {
            b = (byte) (10 + (c - 'A'));
        } else {
            //error
        }
        return b;
    }


    public static void copyHexStringtoBytes(String hexStr, byte[] bytes, int pos, int maxsize) {
        int len = hexStr.length() / 2;
        if (len > maxsize) {
            len = maxsize;
        }
        byte b1;
        byte b2;
        for (int idx = 0, spos = 0; idx < len; ++idx) {
            spos = 2 * idx;
            b1 = hexChartoByte(hexStr.charAt(spos));
            b2 = hexChartoByte(hexStr.charAt(spos + 1));
            bytes[pos + idx] = (byte) (b1 << 4);
            bytes[pos + idx] |= (byte) (b2);
        }
    }


    /**
     * short转换为字节
     *
     * @param num
     * @return
     */
    public static byte[] shortToBytes(short num) {
        byte[] b = new byte[2];
        copyShortToBytes(num, b, 0);
        return b;
    }

    public static void copyShortToBytes(short num, byte[] bytes, int pos) {
        for (int i = 0; i < 2; ++i) {
            bytes[pos + i] = (byte) (num >>> (i * 8));
        }
    }

    public static short bytesToShort(byte[] bytes, int pos) {
        short num = 0;
        for (int i = 0; i < 2; ++i) {
            num |= (bytes[pos + i] & 0xFF) << (i * 8);
        }
        return num;
    }

    /**
     * int类型转换为byte[]类型
     *
     * @param num
     * @return
     */
    public static byte[] intToBytes(int num) {
        byte[] b = new byte[4];
        copyIntToBytes(num, b, 0);
        return b;
    }

    public static void copyIntToBytes(int num, byte[] bytes, int pos) {
        for (int i = 0; i < 4; ++i) {
            bytes[pos + i] = (byte) (num >>> (i * 8));
        }
    }

    public static int bytesToInt(byte[] bytes, int pos) {
        int num = 0;
        for (int i = 0; i < 4; ++i) {
            num |= (bytes[pos + i] & 0xFF) << (i * 8);
        }
        return num;
    }

}
