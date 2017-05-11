package edu.libsys.transform;

import java.io.*;

public class RemoveInvalid {

    //过滤ITEM.txt中非数字的行
    public static void main(String[] args) {
        //配置文件
        String oldFileName = "/home/yuanzhen/project/data/txt/spark/full/paper_paperId_indexTerm.txt";
        String newFileName = "/home/yuanzhen/project/data/txt/spark/full/paper_paperId_indexTerm.txt-new";

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
                bw.write(line
                        .replace("\\", "")
                        .replace("/", "")
                        .replace("；", "")
                        .replace("'", "") + "\n");
                bw.flush();
            }
            //结束
            System.out.println("\nProcessed, bye.");
        } catch (Exception e) {
            System.err.println(line);
            e.printStackTrace();
        }
    }

}
