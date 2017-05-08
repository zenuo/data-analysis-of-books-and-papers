package edu.libsys.transform;

import java.io.*;

/**
 * 获取一半的数据
 */
public class GetHalfOfData {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Useage: java GetHalfOfData sourceFilePath newFilePath\nNow exit.\n");
            return;
        }

        String sourceFilePath = args[0];
        String newFilePath = args[1];

        int flag = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath, true), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (flag == 0) {
                    bw.write(line + "\n");
                    bw.flush();
                    flag = 1;
                } else {
                    flag = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OK, bye.\n");
    }
}
