package edu.libsys.dataclean;

import java.io.*;

public class DataFilter {
    public static void main(String[] args) {
        String sourceFileName = "/home/spark/Project/data/txt/spark/full/paper_paperId_indexTerm.txt";
        String newFileName = "/home/spark/Project/data/txt/spark/half/paper_paperId_indexTerm.txt";

        int flag = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFileName), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName, true), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (flag == 0) {
                    bw.write(line + "\n");
                    flag = 1;
                } else {
                    flag = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
