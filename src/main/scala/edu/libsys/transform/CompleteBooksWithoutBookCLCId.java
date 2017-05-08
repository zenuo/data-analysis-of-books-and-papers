package edu.libsys.transform;

import com.google.common.hash.BloomFilter;
import edu.libsys.util.StringUtil;

import java.io.*;

/**
 * 将没有中图法分类号的图书的中图法分类号用随机字符串填充
 */
public class CompleteBooksWithoutBookCLCId {
    /**
     * 主方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        String bookAuthorNameFilePath = "/home/spark/Project/data/txt/spark/full/book_id_author.txt";
        String bookCLCIdFilePath = "/home/spark/Project/data/txt/spark/full/book_id_CLCId.txt";
        String lackCLCIdBookFilePath = "/home/spark/Project/data/txt/spark/full/book_id_CLCId.txt-lack";

        IntegerFunnel integerFunnel = new IntegerFunnel();
        long expectedInsertions = 2500000;
        BloomFilter<Integer> bloomFilter = BloomFilter.create(integerFunnel, expectedInsertions);

        int randomStringLength = 10;

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
                    lackCLCIdBookBw.write(id + "," + StringUtil.getRandomString(randomStringLength) + "\n");
                    lackCLCIdBookBw.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OK, bye.\n");
    }

    /**
     * 从字符串获取Id
     *
     * @param line      字符串
     * @param delimiter 分割符
     * @return Id
     */
    private static int getId(String line, String delimiter) {
        String[] tokens = line.split(delimiter);
        return Integer.valueOf(tokens[0]);
    }
}
