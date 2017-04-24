package edu.libsys.dataclean;

import com.google.common.hash.BloomFilter;

import java.io.*;

public class BookCLCIdCompletion {
    public static void main(String[] args) {
        String bookAuthorNameFilePath = "/home/spark/Project/data/txt/spark/full/book_id_author.txt";
        String bookCLCIdFilePath = "/home/spark/Project/data/txt/spark/full/book_id_CLCId.txt";
        String lackCLCIdBookFilePath = "/home/spark/Project/data/txt/spark/full/book_id_CLCId.txt-lack";

        IntegerFunnel integerFunnel = new IntegerFunnel();
        long expectedInsertions = 2500000;
        BloomFilter<Integer> bloomFilter = BloomFilter.create(integerFunnel, expectedInsertions);

        try {
            BufferedReader bookAuthorNameBr = new BufferedReader(new InputStreamReader(new FileInputStream(bookAuthorNameFilePath), "UTF-8"));
            BufferedReader bookCLCIdBr = new BufferedReader(new InputStreamReader(new FileInputStream(bookCLCIdFilePath), "UTF-8"));
            BufferedWriter lackCLCIdBookBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lackCLCIdBookFilePath, true), "UTF-8"));
            String line;

            while ((line = bookCLCIdBr.readLine()) != null) {
                bloomFilter.put(getId(line, ","));
            }

            while ((line = bookAuthorNameBr.readLine()) != null) {
                int id = getId(line, "#");
                if (!bloomFilter.mightContain(id)) {
                    lackCLCIdBookBw.write(id + "," + System.nanoTime() + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getId(String line, String delimiter) {
        String[] tokens = line.split(delimiter);
        return Integer.valueOf(tokens[0]);
    }
}
