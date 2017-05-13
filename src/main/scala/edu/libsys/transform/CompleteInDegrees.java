package edu.libsys.transform;

import com.google.common.hash.BloomFilter;

import java.io.*;

public class CompleteInDegrees {

    private static final int offset = 1000000000;

    public static void main(String[] args) {
        String full = "/home/yuanzhen/project/data/txt/spark/full/all.txt";
        String sourceFilePath = "/home/yuanzhen/project/data/indegrees/inDegreesOfPaperPaperGraphByIndexTerm.txt";
        String lackFilePath = sourceFilePath + "-new";

        IntegerFunnel integerFunnel = new IntegerFunnel();
        BloomFilter<Integer> bloomFilter = BloomFilter.create(integerFunnel, 1710000);

        try {
            BufferedReader fullBr = new BufferedReader(new InputStreamReader(new FileInputStream(full), "UTF-8"));
            BufferedReader sourceFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath), "UTF-8"));
            BufferedWriter newFileBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lackFilePath, false), "UTF-8"));

            String line;
            //将源文件的条目ID读入过滤器
            while ((line = sourceFileBr.readLine()) != null) {
                bloomFilter.put(getId(line));
            }

            while ((line = fullBr.readLine()) != null) {
                int id = getId(line);
                if (!bloomFilter.mightContain(id)) {
                    newFileBw.write(String.format("(%d,(0,0,0,0,0,0,0,0))\n", id));
                    newFileBw.flush();
                }
            }
            newFileBw.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("OK, bye.\n");
    }

    private static int getId(String line) {
        String idString = line.replace("(", "").split(",")[0];
        return Integer.valueOf(idString);
    }
}
