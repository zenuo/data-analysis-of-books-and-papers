package edu.libsys.dataclean;

/**
 * Created by spark on 3/11/17.
 * Filter "indexTerm.txt" which in 3264062 lines to "new-indexTerm.txt" 628434 lines.
 */

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.io.*;

public class RemoveDuplicateLine {
    public static void main(String[] args) {
        RemoveDuplicateLine rdl = new RemoveDuplicateLine();
        StringFunnel stringFunnel = new StringFunnel();
        //the capability of BloomFilter
        long expectedInsertions = 3264062;
        BloomFilter<String> bloomFilter = BloomFilter.create(stringFunnel, expectedInsertions);
        //config files
        String oldFileName = "/home/spark/Project/data/txt/id_indexTerm.txt";
        String newFileName = "/home/spark/Project/data/txt/new-id_indexTerm.txt";
        try {
            //traversal file

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(oldFileName), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName, true), "utf-8"));
            String s = null;
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

//for create BloomFilter of String
class StringFunnel implements Funnel<String> {
    public void funnel(String string, PrimitiveSink primitiveSink) {
        primitiveSink.putUnencodedChars(string);
    }
}

//for create BloomFilter of Integer
class IntegerFunnel implements Funnel<Integer> {
    public void funnel(Integer integer, PrimitiveSink primitiveSink) {
        primitiveSink.putInt(integer);
    }
}