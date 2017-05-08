package edu.libsys.dataclean;

import java.io.*;

/**
 * 将作者为空的图书的作者补充为随机字符串
 */
public class CompleteBooksWithoutAuthor {
    /**
     * 主方法，逻辑
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Useage: java CompleteBooksWithoutAuthor sourceFilePath newFilePath\nNow exit.\n");
            return;
        }

        String sourceFilePath = args[0];
        String newFilePath = args[1];

        int randomStringLength = 10;

        try {
            BufferedReader sourceFile = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath), "UTF-8"));
            BufferedWriter newFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath, true), "UTF-8"));
            String line;
            while ((line = sourceFile.readLine()) != null) {
                if ('#' == line.charAt(line.length() - 1)) {
                    newFile.write(String.format("%s%s\n", line, getRandomString(randomStringLength)));
                } else {
                    newFile.write(String.format("%s\n", line));
                }
                newFile.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回指定长度的随机字符串
     *
     * @param length 随机字符串的长度
     * @return 随机字符串
     */
    private static String getRandomString(int length) {
        final char[] CHAR_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        final int CHAR_ARRAY_LENGTH = 62;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHAR_ARRAY[(int) (Math.random() * CHAR_ARRAY_LENGTH)]);
        }
        return stringBuilder.toString();
    }
}
