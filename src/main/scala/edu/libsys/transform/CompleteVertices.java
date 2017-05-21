package edu.libsys.transform;

import com.google.common.hash.BloomFilter;
import edu.libsys.util.StringUtil;

import java.io.*;

public class CompleteVertices {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Useage: java CompleteVertices fullFilePath sourceFilePath lackFilePath verticesType");
            return;
        }

        String fullFilePath = args[0];
        String sourceFilePath = args[1];
        String lackFilePath = args[2];
        String verticesType = args[3];

        IntegerFunnel integerFunnel = new IntegerFunnel();
        long expectedInsertions = 2500000;
        BloomFilter<Integer> bloomFilter = BloomFilter.create(integerFunnel, expectedInsertions);

        try {
            BufferedReader fullFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(fullFilePath), "UTF-8"));
            BufferedReader sourceFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath), "UTF-8"));
            BufferedWriter newFileBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lackFilePath, true), "UTF-8"));

            String line;
            //将源文件的条目ID读入过滤器
            while ((line = sourceFileBr.readLine()) != null) {
                bloomFilter.put(StringUtil.getId(line, ","));
            }

            while ((line = fullFileBr.readLine()) != null) {
                int id = StringUtil.getId(line, ",");
                if (!bloomFilter.mightContain(id)) {
                    newFileBw.write(String.format("%d,0,0,0,%s\n", id, verticesType));
                    newFileBw.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("OK, bye.\n");
    }
}
