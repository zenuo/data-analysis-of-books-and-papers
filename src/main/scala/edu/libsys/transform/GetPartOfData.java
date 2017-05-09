package edu.libsys.transform;

import java.io.*;

/**
 * 获取部分的数据
 */
public class GetPartOfData {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Useage: java GetPartOfData sourceFilePath newFilePath fraction\nNow exit.\n");
            return;
        }

        //源文件路径
        String sourceFilePath = args[0];
        //新的文件路径
        String newFilePath = args[1];
        //新文件的内容占源文件的分数
        String[] fraction = args[2].split("/");

        //分子
        int numerator = Integer.valueOf(fraction[0]);
        //分母
        int denominator = Integer.valueOf(fraction[1]);

        if (numerator > denominator) {
            //若分子大于分母
            System.out.printf("Fraction '%d/%d' is invalid, please make is numerator are less than denominator.\nNow exit.\n", numerator, denominator);
            return;
        } else if (numerator == denominator) {
            //若分子等于于分母
            numerator = 1;
            denominator = 1;
        }
        //标记
        int flag = 1;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath, true), "UTF-8"));
            String line;

            while ((line = br.readLine()) != null) {
                if (flag <= numerator) {
                    //小于分子
                    //写入
                    bw.write(line + "\n");
                    bw.flush();
                    flag++;
                } else if (flag < denominator) {
                    //大于分子且小于分母
                    flag++;
                } else {
                    //等于分母
                    //复位标记
                    flag = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OK, bye.\n");
    }
}
