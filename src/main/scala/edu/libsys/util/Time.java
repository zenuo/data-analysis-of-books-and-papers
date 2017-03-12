package edu.libsys.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Time {
    public static String timestampToString(long time) {
        return new java.text
                .SimpleDateFormat("yyyy-MM-ddHH:mm:ss")
                .format(new java.util.Date(time * 1000));
    }

    public static long stringToTimestamp(String s) {
        try {
            return new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse(s).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void getTime() {
        System.out.println(timestampToString(System.currentTimeMillis() / 1000));
    }
}