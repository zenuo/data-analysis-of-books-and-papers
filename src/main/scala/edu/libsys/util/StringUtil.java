package edu.libsys.util;

/**
 * 字符串工具类
 */
public class StringUtil {
    /**
     * 返回指定长度的随机字符串
     *
     * @param length 随机字符串的长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        final char[] CHAR_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        final int CHAR_ARRAY_LENGTH = 62;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHAR_ARRAY[(int) (Math.random() * CHAR_ARRAY_LENGTH)]);
        }
        return stringBuilder.toString();
    }
}
