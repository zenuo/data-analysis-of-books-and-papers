package edu.libsys.dataclean;

/**
 * Created by spark on 3/11/17.
 * Filter "ITEM.csv" which in 1610018 lines to "ITEM.txt" 387446 lines.
 */

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.io.*;

public class RemoveDuplicateItem {
    public static void main(String[] args) {
        RemoveDuplicateItem rdi = new RemoveDuplicateItem();
        //create bloomfilter
        IntegerFunnel integerFunnel = new IntegerFunnel();
        long expectedInsertions = 1610018;
        BloomFilter<Integer> bloomFilter = BloomFilter.create(integerFunnel, expectedInsertions);
        //traversal file
        try {
            String fileName = "ITEM.csv";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ITEM.txt", true), "utf-8"));
            //write unique item to new file by marc_rec_id
            String s = null;
            while ((s = br.readLine()) != "") {
                int marc_rec_id = Integer.valueOf(rdi.getMarcRecId(s));
                if (!bloomFilter.mightContain(marc_rec_id)) {
                    bloomFilter.put(marc_rec_id);
                    bw.write(s.replace("\"", "") + "\n");
                    bw.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bloomFilter = null;
    }

    private String getMarcRecId(String line) {
        String s = null;
        try {
            return line.split(",")[1].replace("\"", "");
        } catch (Exception e) {
            System.out.println("Error line:");
            System.out.println(line);
            e.printStackTrace();
        }
        return s;
    }
}

class IntegerFunnel implements Funnel<Integer> {
    public void funnel(Integer integer, PrimitiveSink primitiveSink) {
        primitiveSink.putInt(integer);
    }
}