package com.cy.store.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HexUtil {

    private static final char[]						hexChar	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f' };
    private static final Map<Character, Character> hexMap	= new HashMap<Character, Character>();

    public static boolean isHexString(String hexString) {
        String desHex = new String(hexString.toLowerCase());
        if (hexMap.isEmpty()) {
            for (int h = 0; h < hexChar.length; h++) {
                hexMap.put(hexChar[h], hexChar[h]);
            }
        }

        for (int i = 0; i < desHex.length(); i++) {
            if (hexMap.get(desHex.charAt(i)) == null) {
                return false;
            }
        }

        return true;
    }

    public static String byteToHex(byte b) {
        return String.valueOf(hexChar[(b >> 4) & 0xf]).concat(String.valueOf(hexChar[(b) & 0xf]));
    }

    public static String convertAsciiToHexString(String data, String encode) throws UnsupportedEncodingException {
        if (encode == null) {
            return conventBytesToHexString(data.getBytes());
        } else {
            return conventBytesToHexString(data.getBytes(encode));
        }
    }

    public static String conventBytesToHexString(byte[] data) {
        return convertBytesToHexString(data, 0, data.length);
    }

    private static String convertBytesToHexString(byte[] data, int offset, int length) {
        StringBuffer sBuf = new StringBuffer();
        for (int i = offset; i < length; i++) {
            sBuf.append(hexChar[(data[i] >> 4) & 0xf]);
            sBuf.append(hexChar[data[i] & 0xf]);
        }
        return sBuf.toString();
    }

    public static byte[] convertHexStringToBytes(String hexString) {
        return convertHexStringToBytes(hexString, 0, hexString.length());
    }

    public static String convertHexToAsciiString(String hexString, String encode) throws UnsupportedEncodingException {
        return new String(convertHexStringToBytes(hexString, 0, hexString.length()), encode);
    }

    private static byte[] convertHexStringToBytes(String hexString, int offset, int endIndex) {
        byte[] data;
        String realHexString = hexString.substring(offset, endIndex).toLowerCase();
        if ((realHexString.length() % 2) == 0)
            data = new byte[realHexString.length() / 2];
        else
            data = new byte[(int) Math.ceil(realHexString.length() / 2d)];

        int j = 0;
        char[] tmp;
        for (int i = 0; i < realHexString.length(); i += 2) {
            try {
                tmp = realHexString.substring(i, i + 2).toCharArray();
            } catch (StringIndexOutOfBoundsException siob) {
                tmp = (realHexString.substring(i) + "0").toCharArray();
            }
            data[j] = (byte) ((Arrays.binarySearch(hexChar, tmp[0]) & 0xf) << 4);
            data[j++] |= (byte) (Arrays.binarySearch(hexChar, tmp[1]) & 0xf);
        }

        for (int i = realHexString.length(); i > 0; i -= 2) {

        }
        return data;
    }

}
