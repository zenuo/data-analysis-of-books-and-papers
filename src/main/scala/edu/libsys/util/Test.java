package edu.libsys.util;

import org.apache.log4j.PropertyConfigurator;

public class Test {
    //test
    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/scala/edu/libsys/conf/log4j.properties");
        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        System.out.println(StringUtil.getRandomString(18));
    }
}