package edu.libsys.transform;

import java.io.*;

public class RelationshipFileTransform {
    /**
     * 将原来的关联数据转换为新的关联数据
     * 样本:
     * 9,51402,100,0
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Useage: java RelationshipFileTransform sourseFilePath newFilePath\nNow exit.");
            return;
        }
        String sourseFilePath = args[0];
        String newFilePath = args[1];

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourseFilePath), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath, true), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(parseLine(line) + "\n");
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析原文件的一行，丢弃第三个元素后返回
     *
     * @param sourceLine 一行
     * @return 解析后的字符串
     */
    private static String parseLine(final String sourceLine) {
        String[] tokens = sourceLine.split(",");
        return String.format("%s,%s,%s", tokens[0], tokens[1], tokens[3]);
    }
}
