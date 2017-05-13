package edu.libsys.transform;

import java.io.*;

public class GetBookPaperCSV {
    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        String filePath = args[0];
        try {
            //打开文件
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            //新建文件
            BufferedWriter book = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("book.csv", false), "utf-8"));
            BufferedWriter paper = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("paper.csv", false), "utf-8"));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.charAt(line.length() - 1) == 'P') {
                    paper.write(line + "\n");
                } else {
                    book.write(line + "\n");
                }
            }
            br.close();
            book.close();
            paper.close();
            System.out.println("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
