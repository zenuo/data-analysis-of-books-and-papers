package edu.libsys.dataclean;

import java.io.*;

/*LEND_HIST.csv数据样例
“财产号”，“借出时间”，“借阅者ID”，“中图编号”
"99321117","2000-09-0209:54:56","0000040935","N49/81:1"
"13740057","2000-09-1808:41:25","0000042151","I227/126:2"
"13740057","2000-09-1808:41:34","0000020247","C913.14/9"
"99131309","2000-09-1311:12:01","0000010400","B992.3/5"
 */

/*ITEM.txt数据样例
财产号，书籍编号，计数
99000328,0000006280,0
97003188,0000006291,21
20029430,0000006298,0
00058147,0000006303,0
 */

public class RemoveInvalid {

    //过滤ITEM.txt中非数字的行
    public static void main(String[] args) {
        //配置文件
        String oldFileName = "/home/spark/Project/data/txt/book_id_author-title_callId.txt";
        String newFileName = "/home/spark/Project/data/txt/book_id_author-title_callId.txt-new";

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
                //分割
                String[] pieces = line.split("#");
                //判断
                if (pieces.length == 3) {
                    bw.write(line + "\n");
                    bw.flush();
                } else {
                    System.out.println(line);
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
}
