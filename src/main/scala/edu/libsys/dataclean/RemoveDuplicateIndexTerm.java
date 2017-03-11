package edu.libsys.dataclean;

/**
 * Created by spark on 3/11/17.
 * Filter "indexTerm.txt" which in 3264062 lines to "new-indexTerm.txt" 628434 lines.
 */

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.io.*;

public class RemoveDuplicateIndexTerm {
    public static void main(String[] args) {
        RemoveDuplicateIndexTerm rdi = new RemoveDuplicateIndexTerm();
        StringFunnel stringFunnel = new StringFunnel();
        long expectedInsertions = 3264062;
        BloomFilter<String> bloomFilter = BloomFilter.create(stringFunnel, expectedInsertions);
        try {
            //traversal file
            String oldFileName = "/home/spark/Project/data/txt/indexTerm.txt";
            String newFileName = "/home/spark/Project/data/txt/new-indexTerm.txt";
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

class StringFunnel implements Funnel<String> {
    public void funnel(String string, PrimitiveSink primitiveSink) {
        primitiveSink.putUnencodedChars(string);
    }
}