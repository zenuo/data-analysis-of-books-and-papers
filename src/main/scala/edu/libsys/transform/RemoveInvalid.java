package edu.libsys.transform;

import java.io.*;
import java.util.regex.Pattern;

public class RemoveInvalid {

    //过滤ITEM.txt中非数字的行
    public static void main(String[] args) {
        //配置文件
        String oldFileName = "/home/spark/Project/data/txt/book_id_callId.txt";
        String newFileName = "/home/spark/Project/data/txt/book_id_callName.txt";

        //辅助字符串
        String line = null;

        //开始
        System.out.println("Started;\nInvalid lines are:");
        //遍历文件
        try {
            //打开文件
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(oldFileName), "UTF-8"));
            //新建文件
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName, true), "utf-8"));

            //若符合条件则写入新文件
            while ((line = br.readLine()) != null) {
                String[] pieces = line.split(",");
                //判断
                if (isChineseChar(pieces[1])) {
                    bw.write(line + "\n");
                    bw.flush();
                }
            }
            //结束
            System.out.println("\nProcessed, bye.");
        } catch (Exception e) {
            System.err.println(line);
            e.printStackTrace();
        }
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
    private static boolean isChineseChar(String string) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{0,}+");
        return pattern.matcher(string).matches();
    }
}
