package edu.libsys.dataclean;

import com.google.common.hash.BloomFilter;

import java.io.*;

public class RemoveDuplicateLine {
    public static void main(String[] args) {
        StringFunnel stringFunnel = new StringFunnel();
        //the capability of BloomFilter

        long expectedInsertions = 3264062;
        BloomFilter<String> bloomFilter = BloomFilter.create(stringFunnel, expectedInsertions);

        //config files
        String oldFileName = "/home/spark/lack-books";
        String newFileName = "/home/spark/lack-books-new";

        try {
            //traversal file

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(oldFileName), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName, true), "utf-8"));
            String s;
            while ((s = br.readLine()) != null) {
                if (!bloomFilter.mightContain(s)) {
                    bloomFilter.put(s);
                    bw.write(s + "\n");
                    bw.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}