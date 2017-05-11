package edu.libsys.util;

import java.util.regex.Pattern;

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

    /**
     * 从字符串获取Id
     *
     * @param line      字符串
     * @param delimiter 分割符
     * @return Id
     */
    public static int getId(String line, String delimiter) {
        String[] tokens = line.split(delimiter);
        return Integer.valueOf(tokens[0]);
    }


    //判断字符串被切割后的长度是否符合规定
    public static boolean isLengthValid(String string, String delimiter, int length) {
        return string.split(delimiter).length == length;
    }

    //判断字符串是否为整数
    public static boolean isNumeric(String string) {
        for (int i = string.length(); --i >= 0; ) {
            if (!Character.isDigit(string.charAt(i))) {
                System.out.println(string);
                return false;
            }
        }
        return true;
    }

    //是否为汉字
    public static boolean isChineseChar(String string) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{0,}+");
        return pattern.matcher(string).matches();
    }
}
