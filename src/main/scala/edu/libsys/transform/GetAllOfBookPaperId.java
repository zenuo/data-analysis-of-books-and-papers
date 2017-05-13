package edu.libsys.transform;

import java.io.*;

public class GetAllOfBookPaperId {
    private static final int offset = 1000000000;

    public static void main(String[] args) {
        String bookFull = "/home/yuanzhen/project/data/txt/spark/full/book_id.txt";
        String paperFull = "/home/yuanzhen/project/data/txt/spark/full/paper_id_paperId.txt";
        String newFilePath = "/home/yuanzhen/project/data/txt/spark/full/all.txt";
        try {
            BufferedReader bookFullBr = new BufferedReader(new InputStreamReader(new FileInputStream(bookFull), "UTF-8"));
            BufferedReader paperFullBr = new BufferedReader(new InputStreamReader(new FileInputStream(paperFull), "UTF-8"));
            BufferedWriter newFileBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath, false), "UTF-8"));
            String line;
            while ((line = bookFullBr.readLine()) != null) {
                newFileBw.write(getId(line) + "\n");
            }
            while ((line = paperFullBr.readLine()) != null) {
                newFileBw.write(String.format("%d\n", getId(line) + offset));
            }
            newFileBw.close();
            bookFullBr.close();
            paperFullBr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getId(String line) {
        String idString = line.replace("(", "").split(",")[0];
        return Integer.valueOf(idString);
    }
}
