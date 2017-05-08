package edu.libsys.transform;

import edu.libsys.util.StringUtil;

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
        if (args.length != 2) {
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
                    newFile.write(String.format("%s%s\n", line, StringUtil.getRandomString(randomStringLength)));
                } else {
                    newFile.write(String.format("%s\n", line));
                }
                newFile.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("OK, bye.\n");
    }


}
