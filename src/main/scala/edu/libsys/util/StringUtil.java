package edu.libsys.util;

public class StringUtil {
    private static final char[] CHAR_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int CHAR_ARRAY_LENGTH = 62;

    //Return a string of given length.
    public static String getRandomString(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(CHAR_ARRAY[(int) (Math.random() * CHAR_ARRAY_LENGTH)]);
        }
        return stringBuffer.toString();
    }
}
